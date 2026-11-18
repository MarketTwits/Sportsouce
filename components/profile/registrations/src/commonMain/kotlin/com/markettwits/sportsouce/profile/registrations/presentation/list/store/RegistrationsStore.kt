package com.markettwits.sportsouce.profile.registrations.presentation.list.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

interface RegistrationsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
        data object Resume : Intent
        data object Pause : Intent
        data object LoadData : Intent
        data class OnClickFilter(val item: FilterItem) : Intent
        data class OnClickItem(val orderInfo: StartOrderInfo) : Intent
    }

    data class State(
        val base: List<StartOrderInfo>,
        val filtered: List<StartOrderInfo> = base,
        val sharedUser: SharedUser? = null,
        val filter: List<FilterItem> = emptyList(),
        val exception: SauceError? = null,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val isRefreshing: Boolean = false,
    )

    sealed interface Label {
        data object GoBack : Label
        data class OnItemClick(val itemId: StartOrderInfo) : Label
    }
}

class RegistrationsDataStoreFactory(
    private val storeFactory: StoreFactory,
    private val dataSource: StartOrderRegistrationRepository,
    private val exceptionTracker: ExceptionTracker,
) {

    fun create(): RegistrationsStore =
        object : RegistrationsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationsDataStore",
            initialState = State(emptyList()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class Loading(val userInitiated: Boolean) : Msg
        data object LoadFinished : Msg
        data class InfoFailed(val sauceError: SauceError) : Msg
        data class InfoLoaded(val starts: List<StartOrderInfo>, val filtered: List<StartOrderInfo>, val filter: List<FilterItem>) : Msg
        data class UpdateList(val starts: List<StartOrderInfo>, val filter: List<FilterItem>) : Msg
        data class UpdateUserInfo(val sharedUser: SharedUser) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        private var loadJob: Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.Resume -> load(userInitiated = false)
                Intent.Pause -> {
                    loadJob?.cancel()
                    dispatch(Msg.LoadFinished)
                }
                Intent.LoadData -> load(userInitiated = true)
                is Intent.OnClickItem -> publish(Label.OnItemClick(intent.orderInfo))
                is Intent.Pop -> publish(Label.GoBack)
                is Intent.OnClickFilter -> updateFilter(intent.item, state())
            }
        }

        override fun executeAction(action: Unit) {
            scope.launch {
                dataSource.currentUser().onSuccess { user ->
                    dispatch(Msg.UpdateUserInfo(user))
                }
            }
        }

        private fun load(userInitiated: Boolean) {
            loadJob?.cancel()
            loadJob = scope.launch {
                dispatch(Msg.Loading(userInitiated))
                try {
                    dataSource.registrations(forced = userInitiated).collect { starts ->
                        ensureActive()
                        val filter = createFilter(starts, state().filter)
                        dispatch(Msg.InfoLoaded(starts, sortList(starts, filter), filter))
                    }
                    dispatch(Msg.LoadFinished)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    ensureActive()
                    dispatch(Msg.InfoFailed(e.mapToSauceError()))
                    if (!e.isNetworkConnectionError()) {
                        state().sharedUser?.let { user ->
                            exceptionTracker.setUserId(user.id.toString())
                        }
                        exceptionTracker.reportException(e, "RegistrationsDataStoreFactory#launch")
                    }
                }
            }
        }

        private fun createFilter(starts: List<StartOrderInfo>, previous: List<FilterItem>): List<FilterItem> {
            val selected = previous.filter { it.checked }.map { it.value }.toSet()
            return starts.map { it.payment.title }.distinct().sorted().map { title ->
                FilterItem(title, title in selected)
            }
        }

        private fun updateFilter(item: FilterItem, state: State) {
            val index = state.filter.indexOfFirst { it.value == item.value }
            if (index == -1) return
            val filter = state.filter.toMutableList()
            filter[index] = filter[index].copy(checked = !filter[index].checked)
            dispatch(
                Msg.UpdateList(
                    starts = sortList(state.base, filter),
                    filter = filter
                )
            )
        }

        private fun sortList(
            base: List<StartOrderInfo>,
            filter: List<FilterItem>,
        ): List<StartOrderInfo> {
            val checkedFilters = filter.filter { it.checked }
            if (checkedFilters.isEmpty()) return base

            return base.filter { listItem ->
                checkedFilters.any { filter ->
                    listItem.payment.title == filter.value
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {
            return when (msg) {
                is Msg.InfoFailed -> copy(
                    base = emptyList(),
                    filtered = emptyList(),
                    exception = msg.sauceError,
                    isLoading = false,
                    isSuccess = false,
                    isRefreshing = false,
                )

                is Msg.Loading -> copy(
                    isLoading = !isSuccess,
                    isRefreshing = msg.userInitiated && base.isNotEmpty(),
                    exception = null,
                )
                Msg.LoadFinished -> copy(isLoading = false, isRefreshing = false)
                is Msg.UpdateList -> copy(filtered = msg.starts, filter = msg.filter)
                is Msg.InfoLoaded -> copy(
                    base = msg.starts,
                    filtered = msg.filtered,
                    filter = msg.filter,
                    isSuccess = true,
                    isLoading = false,
                )
                is Msg.UpdateUserInfo -> copy(sharedUser = msg.sharedUser)
            }
        }
    }
}

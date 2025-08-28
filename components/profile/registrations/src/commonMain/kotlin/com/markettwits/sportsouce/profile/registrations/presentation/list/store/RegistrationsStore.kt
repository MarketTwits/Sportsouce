package com.markettwits.sportsouce.profile.registrations.presentation.list.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore.*
import kotlinx.coroutines.launch

interface RegistrationsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
        data object LoadData : Intent
        data class OnClickFilter(val item: FilterItem) : Intent
        data class OnClickItem(val orderInfo: StartOrderInfo) : Intent
    }

    data class State(
        val base: List<StartOrderInfo>,
        val filtered: List<StartOrderInfo> = base,
        val sharedUser: SharedUser? = null,
        val filter: List<FilterItem> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = ""
    )

    sealed interface Label {
        data object GoBack : Label
        data class OnItemClick(val itemId: StartOrderInfo) : Label
    }
}

class RegistrationsDataStoreFactory(
    private val storeFactory: StoreFactory,
    private val dataSource: StartOrderRegistrationRepository
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
        data object Loading : Msg
        data class InfoFailed(val message: String) : Msg
        data class InfoLoaded(val starts: List<StartOrderInfo>, val filter: List<FilterItem>) : Msg
        data class UpdateList(val starts: List<StartOrderInfo>, val filter: List<FilterItem>) : Msg
        data class UpdateUserInfo(val sharedUser: SharedUser) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.LoadData -> {
                    launch()
                }

                is Intent.OnClickItem -> publish(Label.OnItemClick(intent.orderInfo))
                is Intent.Pop -> publish(Label.GoBack)
                is Intent.OnClickFilter -> updateFilter(intent.item, state())
            }
        }

        override fun executeAction(action: Unit) {
            launch()
        }

        private fun launch() {
            scope.launch {
                dispatch(Msg.Loading)

                // Collect from registrations Flow
                try {
                    dataSource.registrations().collect { starts ->
                        dispatch(Msg.InfoLoaded(starts = starts, filter = createFilter(starts)))
                    }
                } catch (e: Exception) {
                    dispatch(Msg.InfoFailed(e.message.toString()))
                }

                // Get current user
                dataSource.currentUser().onSuccess { user ->
                    dispatch(Msg.UpdateUserInfo(user))
                }
            }
        }

        private fun createFilter(starts: List<StartOrderInfo>): List<FilterItem> {
            val paymentStatusSet = HashSet<StartOrderPaymentStatus>()
            for (start in starts) {
                paymentStatusSet.add(start.payment)
            }
            return paymentStatusSet.map { paymentStatus ->
                FilterItem(paymentStatus.title, false)
            }.distinctBy { it.value }
        }

        private fun updateFilter(item: FilterItem, state: State) {
            val index = state.filter.indexOf(item)
            val filter = state.filter.toMutableList()
            filter[index] = item.copy(checked = !item.checked)
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
                is Msg.InfoFailed -> State(
                    base = emptyList(),
                    isError = true,
                    isLoading = false,
                    isSuccess = false,
                    message = msg.message
                )

                is Msg.Loading -> copy(isLoading = true, isError = false)
                is Msg.UpdateList -> copy(filtered = msg.starts, filter = msg.filter)
                is Msg.InfoLoaded -> copy(
                    base = msg.starts,
                    filtered = msg.starts,
                    filter = msg.filter,
                    isSuccess = true,
                    isLoading = false,
                    isError = false,
                )
                is Msg.UpdateUserInfo -> copy(sharedUser = msg.sharedUser)
            }
        }
    }
}

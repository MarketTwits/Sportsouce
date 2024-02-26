package com.markettwits.registrations.registrations.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.registrations.registrations.data.StartOrderRegistrationRepository
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations.presentation.components.filter.FilterItem
import com.markettwits.registrations.registrations.presentation.components.filter.baseFilter
import com.markettwits.registrations.registrations.presentation.store.RegistrationsStore.Intent
import com.markettwits.registrations.registrations.presentation.store.RegistrationsStore.Label
import com.markettwits.registrations.registrations.presentation.store.RegistrationsStore.State
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
        val filter: List<FilterItem> = baseFilter,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
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
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.LoadData -> {
                    launch(getState())
                }

                is Intent.OnClickItem -> publish(Label.OnItemClick(intent.orderInfo))
                is Intent.Pop -> publish(Label.GoBack)
                is Intent.OnClickFilter -> updateFilter(intent.item, getState())
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch(getState())
        }

        private fun launch(state: State) {
            scope.launch {
                dispatch(Msg.Loading)
                dataSource.registrations()
                    .onFailure { dispatch(Msg.InfoFailed(it.message.toString())) }
                    .onSuccess { dispatch(Msg.InfoLoaded(starts = it, filter = state.filter)) }
            }
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
                    message = msg.message
                )

                is Msg.Loading -> copy(isLoading = true, isError = false)
                is Msg.UpdateList -> copy(filtered = msg.starts, filter = msg.filter)
                is Msg.InfoLoaded -> copy(
                    base = msg.starts,
                    filtered = msg.starts,
                    filter = msg.filter,
                    isLoading = false,
                    isError = false,
                )
            }
        }
    }
}

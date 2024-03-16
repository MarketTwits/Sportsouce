package com.markettwits.start_filter.start_filter.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import kotlinx.coroutines.launch

internal class StartFilerStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartFilterRepository
) {

    fun create(): StartFilterStore =
        object : StartFilterStore,
            Store<StartFilterStore.Intent, StartFilterStore.State, StartFilterStore.Label> by storeFactory.create(
                name = "StartFilerStore",
                initialState = StartFilterStore.State(isLoading = true, StartFilterUi(emptyList())),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val startFilter: StartFilter) : Msg
        data class InfoFailed(val message: String) : Msg
        data class ChangeFilter(val filter: StartFilterUi) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<StartFilterStore.Intent, Unit, StartFilterStore.State, Msg, StartFilterStore.Label>() {
        override fun executeIntent(
            intent: StartFilterStore.Intent,
            getState: () -> StartFilterStore.State
        ) {
            when (intent) {
                is StartFilterStore.Intent.Launch -> launch()
                is StartFilterStore.Intent.GoBack -> publish(StartFilterStore.Label.GoBack)
                is StartFilterStore.Intent.OnItemSelected -> dispatch(
                    Msg.ChangeFilter(
                        updateState3(
                            item = intent.startFilter,
                            index = intent.index,
                            currentState = getState().filter,
                            singleChoice = intent.singleChoice
                        )
                    )
                )

                StartFilterStore.Intent.Apply -> publish(StartFilterStore.Label.Apply(getState().filter))
                StartFilterStore.Intent.Reset -> dispatch(
                    Msg.ChangeFilter(reset(getState().filter))
                )
            }
        }

        override fun executeAction(action: Unit, getState: () -> StartFilterStore.State) {
            launch()
        }


        private fun updateState3(
            item: String,
            index: Int,
            currentState: StartFilterUi,
            singleChoice: Boolean
        ): StartFilterUi {
            val updatedItems = currentState.items.toMutableList().apply {
                val currentFilterItem = get(index)
                val existingSelected = currentFilterItem.selected.toMutableList()
                if (singleChoice) {
                    existingSelected.clear()
                }
                if (existingSelected.contains(item)) {
                    existingSelected.remove(item)
                } else {
                    existingSelected.add(item)
                }
                val updatedFilterItem = currentFilterItem.copy(selected = existingSelected)
                set(index, updatedFilterItem)
            }
            return currentState.copy(items = updatedItems)
        }

        private fun reset(currentState: StartFilterUi): StartFilterUi {
            val modifiedItems = currentState.items.map { filterGroupItem ->
                filterGroupItem.copy(selected = emptyList())
            }
            return currentState.copy(items = modifiedItems)
        }

        private fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                repository.filter().collect {
                    it.onFailure {
                        dispatch(Msg.InfoFailed(networkExceptionHandler(it).message.toString()))
                    }
                    it.onSuccess {
                        dispatch(Msg.InfoLoaded(it))
                    }
                }

            }
        }
    }

    private object ReducerImpl : Reducer<StartFilterStore.State, Msg> {
        override fun StartFilterStore.State.reduce(message: Msg): StartFilterStore.State =
            when (message) {
                is Msg.InfoFailed -> StartFilterStore.State(
                    isLoading = false,
                    filter = StartFilterUi(emptyList())
                )

                is Msg.Loading -> StartFilterStore.State(
                    isLoading = true,
                    StartFilterUi(emptyList())
                )

                is Msg.InfoLoaded -> StartFilterStore.State(
                    isLoading = false,
                    mapFilterDomainToUi(message.startFilter)
                )

                is Msg.ChangeFilter -> StartFilterStore.State(
                    isLoading = false,
                    filter = message.filter
                )
            }

        fun mapFilterDomainToUi(startFilter: StartFilter): StartFilterUi {
            return StartFilterUi(
                items = listOf(
                    StartFilterUi.FilterGroupItemUi(
                        label = "Вид спорта",
                        type = StartFilterUi.FilterStartType.Dialog,
                        list = startFilter.kindOfSport
                    ),
                    StartFilterUi.FilterGroupItemUi(
                        label = "Сезон",
                        type = StartFilterUi.FilterStartType.Dialog,
                        list = startFilter.startSeason
                    ),
                    StartFilterUi.FilterGroupItemUi(
                        label = "Место проведения",
                        type = StartFilterUi.FilterStartType.DropDown,
                        list = startFilter.city
                    ),
                    StartFilterUi.FilterGroupItemUi(
                        label = "Дата проведения",
                        type = StartFilterUi.FilterStartType.Calendar,
                        list = emptyList()
                    ),
                    StartFilterUi.FilterGroupItemUi(
                        label = "Актуальность",
                        type = StartFilterUi.FilterStartType.DropDown,
                        list = startFilter.startStatus.map { it.title }
                    )
                )
            )
        }
    }
}

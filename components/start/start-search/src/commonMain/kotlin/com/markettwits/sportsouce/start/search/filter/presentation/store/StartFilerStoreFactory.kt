package com.markettwits.sportsouce.start.search.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.domain.StartFilterRepository
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.FilterGroupItemUi
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.FilterStartType
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.Key
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class StartFilerStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartFilterRepository
) {
    fun create(
        filterUi: StartFilterUi?,
    ): StartFilterStore =
        object : StartFilterStore,
            Store<StartFilterStore.Intent, StartFilterStore.State, StartFilterStore.Label> by storeFactory.create(
                name = "StartFilerStore",
                initialState = StartFilterStore.State(
                    isLoading = false,
                    filterUi ?: StartFilterUi(emptyList())
                ),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val startFilter: StartFilter) : Msg
        data object InfoFailed : Msg
        data class ChangeFilter(val filter: StartFilterUi) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<StartFilterStore.Intent, Unit, StartFilterStore.State, Msg, StartFilterStore.Label>() {
        override fun executeIntent(intent: StartFilterStore.Intent) {
            when (intent) {
                is StartFilterStore.Intent.Launch -> launch()
                is StartFilterStore.Intent.GoBack -> publish(StartFilterStore.Label.GoBack)
                is StartFilterStore.Intent.OnItemSelected -> dispatch(
                    Msg.ChangeFilter(
                        updateState(
                            item = intent.startFilter,
                            index = intent.index,
                            currentState = state().filter,
                            singleChoice = intent.singleChoice
                        )
                    )
                )

                is StartFilterStore.Intent.SetFromDate ->
                    dispatch(Msg.ChangeFilter(state().filter.copy(fromDate = intent.date)))
                is StartFilterStore.Intent.SetToDate ->
                    dispatch(Msg.ChangeFilter(state().filter.copy(toDate = intent.date)))
                StartFilterStore.Intent.Apply -> {
                    val current = state()
                    if (!current.isLoading && !current.loadFailed && !current.filter.hasInvalidPeriod) {
                        publish(StartFilterStore.Label.Apply(current.filter, current.filter.sortSelection()))
                    }
                }

                StartFilterStore.Intent.Reset -> dispatch(Msg.ChangeFilter(state().filter.cleared()))
            }
        }

        override fun executeAction(action: Unit) {
            if (state().filter.item(Key.Organizer) == null)
                launch()
        }


        private fun updateState(
            item: String,
            index: Int,
            currentState: StartFilterUi,
            singleChoice: Boolean
        ): StartFilterUi {
            val group = currentState.items.getOrNull(index) ?: return currentState
            if (item.isNotEmpty() && item !in group.list) return currentState
            val selection = when {
                item.isEmpty() -> emptyList()
                singleChoice -> listOf(item)
                item in group.selected -> group.selected - item
                else -> group.selected + item
            }
            val updatedItems = currentState.items.toMutableList()
            updatedItems[index] = group.copy(selected = selection)
            return currentState.copy(items = updatedItems)
        }

        private fun launch() {
            scope.launch {
                repository.filter()
                    .onStart {
                        dispatch(Msg.Loading)
                    }
                    .catch {
                        dispatch(Msg.InfoFailed)
                    }
                    .collect {
                        dispatch(Msg.InfoLoaded(it))
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<StartFilterStore.State, Msg> {
        override fun StartFilterStore.State.reduce(msg: Msg): StartFilterStore.State =
            when (msg) {
                Msg.InfoFailed -> copy(
                    isLoading = false,
                    loadFailed = true,
                )

                is Msg.Loading -> copy(
                    isLoading = true,
                    loadFailed = false,
                )

                is Msg.InfoLoaded -> copy(
                    isLoading = false,
                    filter = filter.copy(items = msg.startFilter.uiItems().map { group ->
                        val previous = group.key?.let(filter::item)?.selected.orEmpty()
                        val selected = if (group.key == Key.Sort && previous.isNotEmpty()) {
                            listOf(sortOptions.first { it.first == filter.sortSelection() }.second)
                        } else previous.filter { it in group.list }
                        group.copy(selected = selected)
                    }),
                    loadFailed = false,
                )

                is Msg.ChangeFilter -> copy(
                    filter = msg.filter
                )
            }
    }
}

private val sortOptions = listOf<Pair<StartFilter.Sorted, String>>(
    StartFilter.Sorted.Popular to "По популярности",
    StartFilter.Sorted.FirstBefore to "Сначала ранние",
    StartFilter.Sorted.LastBefore to "Сначала поздние",
)

private fun StartFilterUi.sortSelection(): StartFilter.Sorted {
    return when (val value = item(Key.Sort)?.selected?.firstOrNull()) {
        "Сначала актуальные" -> StartFilter.Sorted.FirstBefore
        "Сначала прошедшие" -> StartFilter.Sorted.LastBefore
        else -> sortOptions.firstOrNull { it.second == value }?.first ?: StartFilter.Sorted.Popular
    }
}

private fun StartFilter.uiItems(): List<FilterGroupItemUi> = listOf(
    Key.Sport.group(FilterStartType.Dialog, kindOfSport),
    Key.Season.group(FilterStartType.Dialog, startSeason),
    Key.City.group(FilterStartType.DropDown, city),
    Key.Organizer.group(FilterStartType.DropDown, organizers),
    Key.Status.group(FilterStartType.DropDown, startStatus.map { it.title }),
    Key.Sort.group(FilterStartType.DropDown, sortOptions.map { it.second }),
)

private fun Key.group(type: FilterStartType, values: List<String>) =
    FilterGroupItemUi(label = label, type = type, list = values, key = this)

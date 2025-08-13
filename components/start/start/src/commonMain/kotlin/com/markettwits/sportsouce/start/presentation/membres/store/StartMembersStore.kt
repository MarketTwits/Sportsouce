package com.markettwits.sportsouce.start.presentation.membres.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.start.domain.FiltersRemote
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import kotlinx.coroutines.launch
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
import com.markettwits.sportsouce.start.domain.StartMembersPagingParams
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterGroup
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface StartMembersStore : Store<StartMembersStore.Intent, StartMembersStore.State, StartMembersStore.Label> {

    sealed interface Intent {
        data object Init : Intent
        data class QueryChanged(val value: String) : Intent
        data class ApplyFilters(val groups: List<MembersFilterGroup>) : Intent
        data class ResetFilters(val groups: List<MembersFilterGroup>) : Intent
        data class RemoveSelectedFilter(val title: String) : Intent
        data object Back : Intent
    }

    data class State(
        val membersItems: Flow<PagingData<StartMembersUi>>,
        val totalCount: Int = 0,
        val isShowFilter: Boolean = false,
        val queryState: String = "",
        val filtersUi: List<MembersFilterGroup> = emptyList(),
        val selectedFiltersUi: List<MembersFilterGroup> = emptyList(),
    )

    sealed interface Label {
        data object Back : Label
    }
}

class StartMembersStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartRepository,
    private val startId: Int,
) {

    fun create(): StartMembersStore = object : StartMembersStore,
        Store<StartMembersStore.Intent, StartMembersStore.State, StartMembersStore.Label> by storeFactory.create(
            name = "StartMembersStore",
            initialState = StartMembersStore.State(membersItems = flowOf(PagingData.empty())),
            executorFactory = { Executor(repository, startId) },
            reducer = ReducerImpl
        ) {}

    private class Executor(
        private val repo: StartRepository,
        private val startId: Int,
    ) : CoroutineExecutor<StartMembersStore.Intent, Nothing, StartMembersStore.State, Msg, StartMembersStore.Label>(),
        LogTagProvider {

        override val tag: String = "StartMembersExecutor"

        private var filtersRemote: FiltersRemote? = null

        override fun executeIntent(intent: StartMembersStore.Intent) {
            when (intent) {
                is StartMembersStore.Intent.Init -> launchInitial()
                is StartMembersStore.Intent.QueryChanged -> applyQuery(intent.value)
                is StartMembersStore.Intent.ApplyFilters -> applyFilters(intent.groups)
                is StartMembersStore.Intent.ResetFilters -> applyFilters(intent.groups)
                is StartMembersStore.Intent.RemoveSelectedFilter -> removeSelectedByTitle(intent.title)
                is StartMembersStore.Intent.Back -> publish(StartMembersStore.Label.Back)
            }
        }

        private fun launchInitial() {
            scope.launch {
                // load filters first if needed
                repo.membersFilters(startId).onSuccess { remote ->
                    filtersRemote = remote
                    val uiGroups = buildUiGroups(remote)
                    dispatch(Msg.FiltersLoaded(uiGroups))
                    // Initialize selectedFiltersUi with the same data so filters are visible in dialog
                    dispatch(Msg.FiltersSelected(uiGroups))
                }.onFailure { e ->
                    errorLog(e) { "Failed to load members filters" }
                }
                // initial paging flow
                loadPagingFlow()
            }
        }

        private fun loadPagingFlow() {
            scope.launch {
                val remote = filtersRemote
                val st = state()
                val distancesIds = resolveSelectedIds(
                    st.selectedFiltersUi,
                    remote,
                    title = "Дистанция"
                ) { it.distances.mapNotNull { d -> d.id?.let { id -> d.value to id } } }
                val genders = resolveSelectedValues(st.selectedFiltersUi, "Пол")
                val flow = repo.pagingMembers(
                    startId = startId,
                    params = StartMembersPagingParams(
                        query = st.queryState,
                        distances = distancesIds,
                        genders = genders
                    )
                ).map {
                    it.map { d ->
                        dispatch(Msg.UpdateCount(count = d.second))
                        d.first
                    }
                }.cachedIn(scope)
                dispatch(Msg.Loaded(items = flow))
            }
        }

        private fun applyQuery(query: String) {
            scope.launch {
                dispatch(Msg.QueryApplied(query))
                loadPagingFlow()
            }
        }

        private fun applyFilters(groups: List<MembersFilterGroup>) {
            scope.launch {
                dispatch(Msg.FiltersSelected(groups))
                loadPagingFlow()
            }
        }

        private fun removeSelectedByTitle(title: String) {
            scope.launch {
                val current = state().selectedFiltersUi
                val updated = current.map { group ->
                    val newItems = group.items.map { item ->
                        if (item is MembersFilterItem.Selected && item.title == title) {
                            MembersFilterItem.Base(item.title)
                        } else item
                    }
                    group.copy(items = newItems)
                }
                dispatch(Msg.FiltersSelected(updated))
                loadPagingFlow()
            }
        }

        private fun buildUiGroups(filters: FiltersRemote): List<MembersFilterGroup> {
            val groups = mutableListOf<MembersFilterGroup>()
            if (filters.distances.isNotEmpty()) {
                groups += MembersFilterGroup(
                    title = "Дистанция",
                    items = filters.distances.map { MembersFilterItem.Base(it.value) }
                )
            }
            if (filters.teams.isNotEmpty()) {
                groups += MembersFilterGroup(
                    title = "Команда",
                    items = filters.teams.map { MembersFilterItem.Base(it.value) }
                )
            }
            if (filters.cities.isNotEmpty()) {
                groups += MembersFilterGroup(
                    title = "Город",
                    items = filters.cities.map { MembersFilterItem.Base(it.value) }
                )
            }
            if (filters.genders.isNotEmpty()) {
                groups += MembersFilterGroup(
                    title = "Пол",
                    items = filters.genders.map { MembersFilterItem.Base(it.value) }
                )
            }
            return groups
        }

        private fun resolveSelectedValues(
            selected: List<MembersFilterGroup>,
            title: String,
            fallbackTitle: String = title,
            altTitle: String = title,
            key: String? = null,
        ): List<String> {
            val grp = selected.firstOrNull { it.title == title }
            val items = grp?.items?.filterIsInstance<MembersFilterItem.Selected>()?.map { it.title } ?: emptyList()
            return items
        }

        private fun resolveSelectedIds(
            selected: List<MembersFilterGroup>,
            filters: FiltersRemote?,
            title: String,
            pairs: (FiltersRemote) -> List<Pair<String, Int>>,
        ): List<Int> {
            if (filters == null) return emptyList()
            val sel = selected.firstOrNull { it.title == title }?.items?.filterIsInstance<MembersFilterItem.Selected>()
                ?.map { it.title } ?: emptyList()
            if (sel.isEmpty()) return emptyList()
            val dict = pairs(filters).toMap()
            return sel.mapNotNull { dict[it] }
        }
    }

    private sealed interface Msg {
        data class Loaded(val items: Flow<PagingData<StartMembersUi>>) : Msg

        data class UpdateCount(val count: Int) : Msg
        data class FiltersLoaded(val groups: List<MembersFilterGroup>) : Msg
        data class FiltersSelected(val groups: List<MembersFilterGroup>) : Msg
        data class QueryApplied(val value: String) : Msg
    }

    private object ReducerImpl : Reducer<StartMembersStore.State, Msg> {
        override fun StartMembersStore.State.reduce(msg: Msg): StartMembersStore.State = when (msg) {
            is Msg.Loaded -> copy(membersItems = msg.items)
            is Msg.UpdateCount -> copy(totalCount = msg.count)
            is Msg.FiltersLoaded -> copy(filtersUi = msg.groups)
            is Msg.FiltersSelected -> copy(selectedFiltersUi = msg.groups)
            is Msg.QueryApplied -> copy(queryState = msg.value)
        }
    }
}

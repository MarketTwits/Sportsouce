package com.markettwits.sportsouce.start.presentation.result.store

import app.cash.paging.cachedIn
import app.cash.paging.map
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.domain.StartMembersResultsPagingParams
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StartMemberResultsExecutor(
    private val repository: StartRepository,
    private val startId: Int,
    private val initialMemberResult: List<MemberResult>,
    private val filterApi: MemberResultsFilterApi = MemberResultsFilterApi(),
) : CoroutineExecutor<Intent, Nothing, State, Message, Label>() {

    private var initialFilterStateCreated = false

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Init -> {
                if (!initialFilterStateCreated) {
                    val initialFilterState = filterApi.createInitialFilterState(initialMemberResult)
                    dispatch(Message.UpdateFilterState(initialFilterState))
                    initialFilterStateCreated = true
                }
                loadPagingFlow()
            }

            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnChangeQuery -> applyQuery(intent.query)
            is Intent.OnClickBrushQuery -> applyQuery("")
            is Intent.OnToggleFilterDialog -> dispatch(Message.ToggleFilterDialog(!state().isFilterDialogOpen))
            is Intent.OnClickBrush -> clearFilters()
            is Intent.OnDistanceFilterToggle -> toggleDistanceFilter(intent.distanceFilter)
            is Intent.OnGroupFilterToggle -> toggleGroupFilter(intent.groupName)
            is Intent.OnGenderFilterToggle -> toggleGenderFilter(intent.genderName)
        }
    }

    private fun loadPagingFlow() {
        scope.launch {
            val st = state()
            val filterState = st.filterState

            val flow = repository.pagingMembersResults(
                startId = startId,
                params = StartMembersResultsPagingParams(
                    searchQuery = filterState.searchQuery,
                    distance = filterState.distanceFilters.getSelectDistance()?.name ?: "",
                    group = filterState.groupFilters.getSelectedGroups().joinToString(",") { it.name },
                    gender = filterState.genderFilters.getSelectedGenders().joinToString(",") { it.name }
                )
            ).map { pagingData ->
                pagingData.map { (memberResult, count) ->
                    dispatch(Message.UpdateCount(count))
                    memberResult
                }
            }.cachedIn(scope)
            dispatch(Message.Loaded(items = flow))
        }
    }

    private fun applyQuery(query: String) {
        scope.launch {
            dispatch(Message.UpdateFilterState(state().filterState.copy(searchQuery = query)))
            loadPagingFlow()
        }
    }

    private fun clearFilters() {
        scope.launch {
            val currentState = state()
            val clearedFilterState = FilterState(
                distanceFilters = currentState.filterState.distanceFilters.map { it.copy(isSelected = false) },
                groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) },
                genderFilters = currentState.filterState.genderFilters.map { it.copy(isSelected = false) }
            )
            dispatch(Message.UpdateFilterState(clearedFilterState))
            loadPagingFlow()
        }
    }

    private fun toggleDistanceFilter(distanceFilter: DistanceFilter) {
        scope.launch {
            val currentState = state()
            val updatedDistanceFilters = currentState.filterState.distanceFilters.map { distance ->
                if (distance == distanceFilter) {
                    distance.copy(isSelected = !distance.isSelected)
                } else {
                    distance.copy(isSelected = false)
                }
            }

            val newFilterState = currentState.filterState.copy(
                distanceFilters = updatedDistanceFilters,
                groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) }
            )

            dispatch(Message.UpdateFilterState(newFilterState))
            loadPagingFlow()
        }
    }

    private fun toggleGroupFilter(groupName: String) {
        scope.launch {
            val currentState = state()
            val updatedGroupFilters = currentState.filterState.groupFilters.map { group ->
                if (group.name == groupName) {
                    group.copy(isSelected = !group.isSelected)
                } else {
                    group
                }
            }
            val newFilterState = currentState.filterState.copy(groupFilters = updatedGroupFilters)
            dispatch(Message.UpdateFilterState(newFilterState))
            loadPagingFlow()
        }
    }

    private fun toggleGenderFilter(genderName: String) {
        scope.launch {
            val currentState = state()
            val updatedGenderFilters = currentState.filterState.genderFilters.map { gender ->
                if (gender.name == genderName) {
                    gender.copy(isSelected = !gender.isSelected)
                } else {
                    gender
                }
            }
            val newFilterState = currentState.filterState.copy(genderFilters = updatedGenderFilters)
            dispatch(Message.UpdateFilterState(newFilterState))
            loadPagingFlow()
        }
    }
}
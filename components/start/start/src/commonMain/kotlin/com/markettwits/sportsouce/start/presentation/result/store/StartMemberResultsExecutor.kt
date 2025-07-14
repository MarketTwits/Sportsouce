package com.markettwits.sportsouce.start.presentation.result.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.Message.*

class StartMemberResultsExecutor(
    private val filterApi: MemberResultsFilterApi = MemberResultsFilterApi(),
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> {
                publish(Label.GoBack)
            }

            is Intent.OnMembersLoaded -> {
                val initialFilterState = filterApi.createInitialFilterState(intent.members)
                dispatch(UpdateMembersResult(intent.members))
                dispatch(UpdateFilterState(initialFilterState))
                applyFilters(intent.members, initialFilterState)
            }

            is Intent.OnChangeQuery -> {
                val newFilterState = state().filterState.copy(searchQuery = intent.query)
                dispatch(UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is Intent.OnClickBrush -> {
                val currentState = state()
                val clearedFilterState = FilterState(
                    distanceFilters = currentState.filterState.distanceFilters.map { it.copy(isSelected = false) },
                    groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) },
                    teamFilters = currentState.filterState.teamFilters.map { it.copy(isSelected = false) }
                )
                dispatch(UpdateFilterState(clearedFilterState))
                applyFilters(currentState.membersResult, clearedFilterState)
            }

            is Intent.OnToggleFilterDialog -> {
                dispatch(ToggleFilterDialog(!state().isFilterDialogOpen))
            }

            is Intent.OnDistanceFilterToggle -> {
                val currentState = state()

                // Обновляем выбор дистанции
                val updatedDistanceFilters = currentState.filterState.distanceFilters.map { distance ->
                    if (distance == intent.distanceFilter) {
                        distance.copy(isSelected = !distance.isSelected)
                    } else {
                        distance.copy(isSelected = false)
                    }
                }

                val selectedDistance = updatedDistanceFilters.firstOrNull { it.isSelected }?.name

                val updatedGroupFilters = currentState.membersResult
                    .filter { it.distance == selectedDistance }
                    .map { it.group }
                    .distinct()
                    .map { groupName -> GroupFilter(name = groupName, isSelected = false) }

                val newFilterState = currentState.filterState.copy(
                    distanceFilters = updatedDistanceFilters,
                    groupFilters = updatedGroupFilters
                )

                dispatch(UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is Intent.OnGroupFilterToggle -> {
                val currentState = state()
                val updatedGroupFilters = currentState.filterState.groupFilters.map { group ->
                    if (group.name == intent.groupName) {
                        group.copy(isSelected = !group.isSelected)
                    } else {
                        group
                    }
                }
                val newFilterState = currentState.filterState.copy(groupFilters = updatedGroupFilters)
                dispatch(UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is Intent.OnTeamFilterToggle -> {
                val currentState = state()
                val updatedTeamFilters = currentState.filterState.teamFilters.map { team ->
                    if (team.name == intent.teamName) {
                        team.copy(isSelected = !team.isSelected)
                    } else {
                        team
                    }
                }
                val newFilterState = currentState.filterState.copy(teamFilters = updatedTeamFilters)
                dispatch(UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is Intent.OnSortByChange -> {
                val newFilterState = state().filterState.copy(sortBy = intent.sortBy)
                dispatch(UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is Intent.OnSortOrderChange -> {
                val newFilterState = state().filterState.copy(sortOrder = intent.sortOrder)
                dispatch(UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is Intent.OnClearFilters -> {
                val currentState = state()
                val clearedFilterState = FilterState(
                    distanceFilters = currentState.filterState.distanceFilters.map { it.copy(isSelected = false) },
                    groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) },
                    teamFilters = currentState.filterState.teamFilters.map { it.copy(isSelected = false) }
                )
                dispatch(UpdateFilterState(clearedFilterState))
                applyFilters(currentState.membersResult, clearedFilterState)
            }

            is Intent.OnClickBrushQuery -> {val currentState = state()
                val clearedFilterState = state().filterState.copy(searchQuery = "")
                dispatch(UpdateFilterState(clearedFilterState))
                applyFilters(currentState.membersResult, clearedFilterState)}
        }
    }

    override fun executeAction(action: Unit) {
        super.executeAction(action)
        val members = state().membersResult
        val initialFilterState = filterApi.createInitialFilterState(members)
        dispatch(Message.UpdateMembersResult(members))
        dispatch(Message.UpdateFilterState(initialFilterState))
        applyFilters(members, initialFilterState)
    }

    private fun applyFilters(members: List<MemberResult>, filterState: FilterState) {
        val filteredMembers = filterApi.filterAndSort(members, filterState)
        dispatch(Message.UpdateFilteredMembers(filteredMembers))
    }
}
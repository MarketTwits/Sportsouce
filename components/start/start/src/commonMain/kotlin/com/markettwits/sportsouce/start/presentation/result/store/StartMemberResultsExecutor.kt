package com.markettwits.sportsouce.start.presentation.result.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*

class StartMemberResultsExecutor(
    private val filterApi: MemberResultsFilterApi = MemberResultsFilterApi(),
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is StartMemberResultsStore.Intent.OnClickGoBack -> {
                publish(StartMemberResultsStore.Label.GoBack)
            }

            is StartMemberResultsStore.Intent.OnMembersLoaded -> {
                // Инициализируем фильтры на основе загруженных данных
                val initialFilterState = filterApi.createInitialFilterState(intent.members)
                dispatch(Message.UpdateMembersResult(intent.members))
                dispatch(Message.UpdateFilterState(initialFilterState))
                applyFilters(intent.members, initialFilterState)
            }

            is StartMemberResultsStore.Intent.OnChangeQuery -> {
                val newFilterState = state().filterState.copy(searchQuery = intent.query)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnClickBrush -> {
                // Сброс всех фильтров, но сохраняем доступные опции
                val currentState = state()
                val clearedFilterState = FilterState(
                    distanceFilters = currentState.filterState.distanceFilters.map { it.copy(isSelected = false) },
                    groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) },
                    teamFilters = currentState.filterState.teamFilters.map { it.copy(isSelected = false) }
                )
                dispatch(Message.UpdateFilterState(clearedFilterState))
                applyFilters(currentState.membersResult, clearedFilterState)
            }

            is StartMemberResultsStore.Intent.OnToggleFilterDialog -> {
                dispatch(Message.ToggleFilterDialog(!state().isFilterDialogOpen))
            }

            is StartMemberResultsStore.Intent.OnGenderFilterChange -> {
                val newFilterState = state().filterState.copy(genderFilter = intent.gender)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnDistanceFilterToggle -> {
                val currentState = state()
                val updatedDistanceFilters = currentState.filterState.distanceFilters.map { distance ->
                    if (distance.name == intent.distanceName) {
                        distance.copy(isSelected = !distance.isSelected)
                    } else {
                        distance
                    }
                }
                val newFilterState = currentState.filterState.copy(distanceFilters = updatedDistanceFilters)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnGroupFilterToggle -> {
                val currentState = state()
                val updatedGroupFilters = currentState.filterState.groupFilters.map { group ->
                    if (group.name == intent.groupName) {
                        group.copy(isSelected = !group.isSelected)
                    } else {
                        group
                    }
                }
                val newFilterState = currentState.filterState.copy(groupFilters = updatedGroupFilters)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnTeamFilterToggle -> {
                val currentState = state()
                val updatedTeamFilters = currentState.filterState.teamFilters.map { team ->
                    if (team.name == intent.teamName) {
                        team.copy(isSelected = !team.isSelected)
                    } else {
                        team
                    }
                }
                val newFilterState = currentState.filterState.copy(teamFilters = updatedTeamFilters)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(currentState.membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnSortByChange -> {
                val newFilterState = state().filterState.copy(sortBy = intent.sortBy)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnSortOrderChange -> {
                val newFilterState = state().filterState.copy(sortOrder = intent.sortOrder)
                dispatch(Message.UpdateFilterState(newFilterState))
                applyFilters(state().membersResult, newFilterState)
            }

            is StartMemberResultsStore.Intent.OnClearFilters -> {
                val currentState = state()
                val clearedFilterState = FilterState(
                    distanceFilters = currentState.filterState.distanceFilters.map { it.copy(isSelected = false) },
                    groupFilters = currentState.filterState.groupFilters.map { it.copy(isSelected = false) },
                    teamFilters = currentState.filterState.teamFilters.map { it.copy(isSelected = false) }
                )
                dispatch(Message.UpdateFilterState(clearedFilterState))
                applyFilters(currentState.membersResult, clearedFilterState)
            }
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
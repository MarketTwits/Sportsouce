package com.markettwits.sportsouce.start.presentation.result.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.start.presentation.result.model.DistanceFilter
import com.markettwits.sportsouce.start.presentation.result.model.FilterState
import com.markettwits.sportsouce.start.presentation.result.model.GenderFilter
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.model.SortBy
import com.markettwits.sportsouce.start.presentation.result.model.SortOrder
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*

interface StartMemberResultsStore : Store<Intent, State, Label> {

    data class State(
        val membersResult: List<MemberResult> = emptyList(),
        val filteredMembers: List<MemberResult> = emptyList(),
        val filterState: FilterState = FilterState(),
        val isFilterDialogOpen: Boolean = false
    )

    sealed interface Intent {
        data object OnClickGoBack : Intent
        data class OnChangeQuery(val query: String) : Intent
        data object OnClickBrush : Intent
        data object OnToggleFilterDialog : Intent

        // Filter intents
        data class OnGenderFilterChange(val gender: GenderFilter) : Intent
        data class OnDistanceFilterToggle(val distanceName: String) : Intent
        data class OnGroupFilterToggle(val groupName: String) : Intent
        data class OnTeamFilterToggle(val teamName: String) : Intent
        data class OnSortByChange(val sortBy: SortBy) : Intent
        data class OnSortOrderChange(val sortOrder: SortOrder) : Intent
        data object OnClearFilters : Intent
        data class OnMembersLoaded(val members: List<MemberResult>) : Intent
    }

    sealed interface Message {
        data class UpdateMembersResult(val membersResult: List<MemberResult>) : Message
        data class UpdateFilterState(val filterState: FilterState) : Message
        data class UpdateFilteredMembers(val members: List<MemberResult>) : Message
        data class ToggleFilterDialog(val isOpen: Boolean) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}

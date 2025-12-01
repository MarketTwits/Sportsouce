package com.markettwits.sportsouce.start.presentation.result.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.start.presentation.result.model.DistanceFilter
import com.markettwits.sportsouce.start.presentation.result.model.FilterState
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*
import kotlinx.coroutines.flow.Flow

interface StartMemberResultsStore : Store<Intent, State, Label> {

    data class State(
        val membersItems: Flow<PagingData<MemberResult>>,
        val totalCount: Int = 0,
        val filterState: FilterState = FilterState(),
        val isFilterDialogOpen: Boolean = false,
    )

    sealed interface Intent {
        data object Init : Intent
        data object OnClickGoBack : Intent
        data class OnChangeQuery(val query: String) : Intent
        data object OnClickBrushQuery : Intent
        data object OnToggleFilterDialog : Intent
        data object OnClickBrush : Intent

        // Filter intents
        data class OnDistanceFilterToggle(val distanceFilter: DistanceFilter) : Intent
        data class OnGroupFilterToggle(val groupName: String) : Intent
        data class OnGenderFilterToggle(val genderName: String) : Intent
    }

    sealed interface Message {
        data class Loaded(val items: Flow<PagingData<MemberResult>>) : Message
        data class UpdateCount(val count: Int) : Message
        data class UpdateFilterState(val filterState: FilterState) : Message
        data class ToggleFilterDialog(val isOpen: Boolean) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}

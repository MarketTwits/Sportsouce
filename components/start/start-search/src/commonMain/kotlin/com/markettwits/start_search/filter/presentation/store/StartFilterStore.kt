package com.markettwits.start_search.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.presentation.component.StartFilterUi


interface StartFilterStore :
    Store<StartFilterStore.Intent, StartFilterStore.State, StartFilterStore.Label> {

    sealed interface Intent {
        data class OnItemSelected(
            val startFilter: String,
            val index: Int,
            val singleChoice: Boolean = true
        ) : Intent

        data object Launch : Intent
        data object GoBack : Intent
        data object Apply : Intent
        data object Reset : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val filter: StartFilterUi
    )

    sealed interface Label {
        data class Apply(val item: StartFilterUi, val sorted: StartFilter.Sorted) : Label
        data object GoBack : Label
    }
}


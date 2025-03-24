package com.markettwits.start_search.search.presentation.component

import com.markettwits.start_search.search.presentation.store.StartsSearchStore
import kotlinx.coroutines.flow.StateFlow

interface StartsSearchComponent {
    val model: StateFlow<StartsSearchStore.State>
    fun obtainEvent(intent: StartsSearchStore.Intent)
}

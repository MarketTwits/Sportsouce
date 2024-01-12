package com.markettwits.start_filter.starts

import com.markettwits.start_filter.start_filter.presentation.store.StartFilterStore
import com.markettwits.start_filter.starts.store.StartsFilteredStore
import kotlinx.coroutines.flow.StateFlow

interface StartsFilteredComponent {
    fun obtainEvent(event: StartsFilteredStore.Intent)
    val value: StateFlow<StartsFilteredStore.State>
}
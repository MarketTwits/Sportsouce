package com.markettwits.sportsouce.start.filter.starts

import com.markettwits.sportsouce.start.filter.starts.store.StartsFilteredStore
import kotlinx.coroutines.flow.StateFlow

interface StartsFilteredComponent {
    fun obtainEvent(event: StartsFilteredStore.Intent)
    val value: StateFlow<StartsFilteredStore.State>
}
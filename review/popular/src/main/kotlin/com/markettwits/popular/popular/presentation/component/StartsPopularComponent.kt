package com.markettwits.popular.popular.presentation.component

import com.markettwits.popular.popular.presentation.store.StartsPopularStore
import kotlinx.coroutines.flow.StateFlow

interface StartsPopularComponent {
    val state: StateFlow<StartsPopularStore.State>
    fun obtainEvent(intent: StartsPopularStore.Intent)
}
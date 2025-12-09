package com.markettwits.sportsouce.starts.favorites.presentation.component

import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore
import kotlinx.coroutines.flow.StateFlow

interface StartsFavoritesComponent {
    val state: StateFlow<StartsPopularStore.State>
    fun obtainEvent(intent: StartsPopularStore.Intent)
}
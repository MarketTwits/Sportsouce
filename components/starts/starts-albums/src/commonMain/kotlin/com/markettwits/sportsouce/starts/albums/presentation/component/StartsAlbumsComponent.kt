package com.markettwits.sportsouce.starts.albums.presentation.component

import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStore
import kotlinx.coroutines.flow.StateFlow

interface StartsAlbumsComponent {
    val state: StateFlow<StartsAlbumsStore.State>
    fun obtainEvent(intent: StartsAlbumsStore.Intent)
}

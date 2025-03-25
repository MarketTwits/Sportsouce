package com.markettwits.sportsouce.start.presentation.album.presentation.component

import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import kotlinx.coroutines.flow.StateFlow

interface StartAlbumComponent {
    val state: StateFlow<StartAlbumStore.State>
    fun obtainEvent(intent: StartAlbumStore.Intent)
}
package com.markettwits.sportsouce.starts.albums.filter.component

import com.markettwits.sportsouce.starts.albums.filter.store.AlbumsFilterStore
import kotlinx.coroutines.flow.StateFlow

interface AlbumsFilterComponent {
    val state: StateFlow<AlbumsFilterStore.State>
    fun obtainEvent(intent: AlbumsFilterStore.Intent)
}

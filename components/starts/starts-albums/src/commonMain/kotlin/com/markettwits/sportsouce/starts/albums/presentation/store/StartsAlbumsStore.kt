package com.markettwits.sportsouce.starts.albums.presentation.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.starts.albums.domain.StartAlbumsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface StartsAlbumsStore : Store<StartsAlbumsStore.Intent, StartsAlbumsStore.State, StartsAlbumsStore.Label> {
    data class State(
        val filterText: String = "",
        val year: Int? = null,
        val items: Flow<PagingData<StartAlbumsItem>> = flowOf(PagingData.empty()),
    )
    sealed interface Intent {
        data object Back : Intent
        data object OpenFilters : Intent
        data class Search(val text: String) : Intent
        data class SelectYear(val year: Int?) : Intent
        data class OpenAlbum(val album: StartAlbumsItem.Album) : Intent
    }
    sealed interface Message {
        data class QueryChanged(val text: String) : Message
        data class YearChanged(val year: Int?) : Message
        data class Loaded(val items: Flow<PagingData<StartAlbumsItem>>) : Message
    }
    sealed interface Label {
        data object Back : Label
        data class OpenFilters(val year: Int?) : Label
        data class OpenAlbum(val id: Int) : Label
    }
}

package com.markettwits.sportsouce.starts.albums.presentation.store

import app.cash.paging.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.starts.albums.domain.*
import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStore.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class StartsAlbumsExecutor(private val repository: StartsAlbumsRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var searchJob: Job? = null
    override fun executeAction(action: Unit) = load()
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.Back -> publish(Label.Back)
            Intent.OpenFilters -> publish(Label.OpenFilters(state().year))
            is Intent.OpenAlbum -> if (intent.album.photosCount > 0) publish(Label.OpenAlbum(intent.album.id))
            is Intent.Search -> {
                if (intent.text == state().filterText) return
                dispatch(Message.QueryChanged(intent.text))
                searchJob?.cancel()
                searchJob = scope.launch { delay(350); load() }
            }
            is Intent.SelectYear -> {
                if (intent.year == state().year) return
                searchJob?.cancel()
                dispatch(Message.YearChanged(intent.year))
                load()
            }
        }
    }
    private fun load() {
        val query = state().filterText.trim()
        val year = state().year
        val pager = Pager<Int, StartAlbumsItem>(PagingConfig(pageSize = 10, initialLoadSize = 10,
            prefetchDistance = 3, enablePlaceholders = false)) {
            StartsAlbumsPagingSource(repository, query, year, 10)
        }
        dispatch(Message.Loaded(pager.flow.map { data -> data.filter { it.albums.isNotEmpty() } }.cachedIn(scope)))
    }
}

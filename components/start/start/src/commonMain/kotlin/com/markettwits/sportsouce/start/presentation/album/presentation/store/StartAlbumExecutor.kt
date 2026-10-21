package com.markettwits.sportsouce.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumRepository
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class StartAlbumExecutor(private val albumId: Int?, private val repository: StartAlbumRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) = load()
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.GoBack -> publish(Label.GoBack)
            Intent.Retry -> load()
        }
    }
    private fun load() {
        val id = albumId ?: return
        if (state().isLoading) return
        dispatch(Message.Loading)
        scope.launch {
            try { dispatch(Message.Loaded(repository.images(id))) }
            catch (e: Exception) {
                if (e is CancellationException) throw e
                dispatch(Message.Failed("Не удалось загрузить фотографии. Повторите попытку."))
            }
        }
    }
}

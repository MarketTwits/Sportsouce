package com.markettwits.sportsouce.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.Message
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.State

object StartAlbumReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        Message.Loading -> copy(isLoading = true, error = null)
        is Message.Loaded -> copy(album = msg.album, isLoading = false, error = null)
        is Message.Failed -> copy(isLoading = false, error = msg.error)
        is Message.Photos -> copy(photos = msg.items, selectedTags = msg.tags, filteredCount = null, selectedPhotos = emptyMap())
        is Message.Count -> copy(filteredCount = msg.count)
        is Message.Selection -> copy(selectedPhotos = msg.photos)
        is Message.Exporting -> copy(exportProgress = msg.progress, exportError = null, exportAction = msg.action, downloadResult = null)
        is Message.ExportFinished -> copy(exportProgress = null, exportError = null, downloadResult = msg.result)
        is Message.ExportFailed -> copy(exportProgress = null, exportError = msg.error)
    }
}

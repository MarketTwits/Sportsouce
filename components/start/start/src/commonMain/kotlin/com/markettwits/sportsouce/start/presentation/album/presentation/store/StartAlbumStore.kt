package com.markettwits.sportsouce.start.presentation.album.presentation.store

import app.cash.paging.PagingData
import com.markettwits.OriginalImageShareProgress
import com.markettwits.OriginalImageDownloadResult
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface StartAlbumStore : Store<StartAlbumStore.Intent, StartAlbumStore.State, StartAlbumStore.Label> {
    enum class ExportAction { Share, Download }
    data class State(
        val images: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val album: StartAlbumDetails? = null,
        val photos: Flow<PagingData<AlbumPhoto>> = flowOf(PagingData.empty()),
        val selectedTags: Set<String> = emptySet(),
        val filteredCount: Int? = null,
        val selectedPhotos: Map<Int, AlbumPhoto> = emptyMap(),
        val exportProgress: OriginalImageShareProgress? = null,
        val exportError: String? = null,
        val exportAction: ExportAction = ExportAction.Share,
        val downloadResult: OriginalImageDownloadResult? = null,
    )
    sealed interface Intent {
        data object GoBack : Intent
        data object Retry : Intent
        data class ToggleTag(val tag: String) : Intent
        data object ClearTags : Intent
        data class TogglePhoto(val photo: AlbumPhoto) : Intent
        data class SelectPhotos(val photos: List<AlbumPhoto>) : Intent
        data object ClearSelection : Intent
        data class SharePhotos(val photos: List<AlbumPhoto>) : Intent
        data object ShareSelection : Intent
        data class DownloadPhotos(val photos: List<AlbumPhoto>) : Intent
        data object DownloadSelection : Intent
        data object RetryExport : Intent
        data object CancelExport : Intent
    }
    sealed interface Message {
        data object Loading : Message
        data class Loaded(val album: StartAlbumDetails) : Message
        data class Failed(val error: String) : Message
        data class Photos(val items: Flow<PagingData<AlbumPhoto>>, val tags: Set<String>) : Message
        data class Count(val count: Int) : Message
        data class Selection(val photos: Map<Int, AlbumPhoto>) : Message
        data class Exporting(val progress: OriginalImageShareProgress, val action: ExportAction) : Message
        data class ExportFinished(val result: OriginalImageDownloadResult? = null) : Message
        data class ExportFailed(val error: String) : Message
    }
    sealed interface Label { data object GoBack : Label }
}

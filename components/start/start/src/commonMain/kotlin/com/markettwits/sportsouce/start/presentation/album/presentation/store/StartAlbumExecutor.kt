package com.markettwits.sportsouce.start.presentation.album.presentation.store

import app.cash.paging.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.*
import kotlinx.coroutines.*
import com.markettwits.OriginalImageSharer
import com.markettwits.OriginalImageShareProgress
import com.markettwits.OriginalImageDownloader

class StartAlbumExecutor(private val albumId: Int?, private val repository: StartAlbumRepository,
    private val sharer: OriginalImageSharer = OriginalImageSharer.Unavailable,
    private val downloader: OriginalImageDownloader = OriginalImageDownloader.Unavailable) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var generation = 0
    private var pagingJob: Job? = null
    private var exportJob: Job? = null
    private var lastExportPhotos = emptyList<AlbumPhoto>()
    override fun executeAction(action: Unit) = load()
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.GoBack -> when {
                state().exportProgress != null || state().exportError != null || state().downloadResult != null -> cancelExport()
                state().selectedPhotos.isNotEmpty() -> dispatch(Message.Selection(emptyMap()))
                else -> publish(Label.GoBack)
            }
            Intent.Retry -> load()
            is Intent.ToggleTag -> {
                if (intent.tag !in state().album?.tags.orEmpty()) return
                val tags = state().selectedTags
                photos(if (intent.tag in tags) tags - intent.tag else tags + intent.tag)
            }
            Intent.ClearTags -> if (state().selectedTags.isNotEmpty()) photos(emptySet())
            is Intent.TogglePhoto -> if (state().exportProgress == null) {
                val selected = state().selectedPhotos
                dispatch(Message.Selection(if (intent.photo.id in selected) selected - intent.photo.id else selected + (intent.photo.id to intent.photo)))
            }
            is Intent.SelectPhotos -> if (state().exportProgress == null) dispatch(Message.Selection(intent.photos.associateBy { it.id }))
            Intent.ClearSelection -> if (state().exportProgress == null) dispatch(Message.Selection(emptyMap()))
            Intent.ShareSelection -> export(state().selectedPhotos.values.toList(), ExportAction.Share)
            is Intent.SharePhotos -> export(intent.photos, ExportAction.Share)
            Intent.DownloadSelection -> export(state().selectedPhotos.values.toList(), ExportAction.Download)
            is Intent.DownloadPhotos -> export(intent.photos, ExportAction.Download)
            Intent.RetryExport -> export(lastExportPhotos, state().exportAction)
            Intent.CancelExport -> cancelExport()
        }
    }
    private fun cancelExport() {
        exportJob?.cancel(); exportJob = null
        dispatch(Message.ExportFinished())
    }
    private fun export(photos: List<AlbumPhoto>, action: ExportAction) {
        if (state().exportProgress != null || photos.isEmpty()) return
        lastExportPhotos = photos.toList()
        dispatch(Message.Exporting(OriginalImageShareProgress(0, photos.size), action))
        exportJob = scope.launch {
            try {
                val job = currentCoroutineContext()[Job]
                val progress: (OriginalImageShareProgress) -> Unit = { progress ->
                    // Download callbacks may come from IO; Store dispatch stays on its main scope.
                    scope.launch { if (job?.isActive == true) dispatch(Message.Exporting(progress, action)) }
                }
                val urls = photos.map { it.url }
                val saved = when (action) {
                    ExportAction.Share -> { sharer.share(urls, progress); null }
                    ExportAction.Download -> downloader.download(urls, progress)
                }
                dispatch(Message.ExportFinished(saved))
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                val message = if (action == ExportAction.Download) "Не удалось сохранить фотографии. Проверьте соединение, свободное место и разрешение на сохранение фото."
                    else "Не удалось загрузить все оригиналы. Проверьте соединение и повторите попытку."
                dispatch(Message.ExportFailed(message))
            }
        }
    }
    private fun load() {
        val id = albumId ?: return
        if (state().isLoading) return
        dispatch(Message.Loading)
        scope.launch {
            try {
                val album = repository.album(id)
                dispatch(Message.Loaded(album))
                photos(state().selectedTags.intersect(album.tags.toSet()))
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                dispatch(Message.Failed("Не удалось загрузить альбом. Повторите попытку."))
            }
        }
    }
    private fun photos(tags: Set<String>) {
        val id = albumId ?: return
        val currentGeneration = ++generation
        pagingJob?.cancel()
        val job = SupervisorJob(scope.coroutineContext[Job]).also { pagingJob = it }
        val pagingScope = CoroutineScope(scope.coroutineContext + job)
        val orderedTags = state().album?.tags.orEmpty().filter { it in tags }
        val pager = Pager<Int, AlbumPhoto>(PagingConfig(pageSize = 30, initialLoadSize = 30,
            prefetchDistance = 6, enablePlaceholders = false)) {
            AlbumPhotosPagingSource(repository, id, orderedTags) { count ->
                // Responses from an earlier selection cannot overwrite the current counter.
                scope.launch { if (currentGeneration == generation) dispatch(Message.Count(count)) }
            }
        }
        dispatch(Message.Photos(pager.flow.cachedIn(pagingScope), tags))
    }
}

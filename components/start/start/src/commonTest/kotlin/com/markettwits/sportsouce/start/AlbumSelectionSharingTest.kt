package com.markettwits.sportsouce.start

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.*
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.components.albumSelectionRange
import com.markettwits.sportsouce.start.presentation.album.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumSelectionSharingTest {
    private val photos = List(10) { AlbumPhoto(it + 1, "https://cdn.sportsauce.ru/${it + 1}.jpg") }
    private val repo = object : StartAlbumRepository {
        override suspend fun album(albumId: Int) = error("unused")
        override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>) = AlbumPhotosPage(emptyList(), 0)
    }

    @Test fun albumSharingUsesItsCanonicalPublicLinkAndKeepsPhotoSelection() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val sent = mutableListOf<String>()
        val action = object : IntentAction {
            override fun openWebPage(url: String) = error("Must open the system share sheet")
            override fun openPhone(phone: String) = error("unused")
            override fun shareImage(byteArray: ByteArray, filename: String) = error("Link sharing must not download a photo")
            override fun saveImage(byteArray: ByteArray, filename: String) = error("unused")
            override fun copyToSystemBuffer(text: String) = error("Must share instead of copying")
            override fun sharePlainText(text: String) { sent += text }
        }
        val repository = object : StartAlbumRepository {
            override suspend fun album(albumId: Int) = StartAlbumDetails("Фото", "Трейл", "", 0, emptyList())
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>) = AlbumPhotosPage(emptyList(), 0)
        }
        try {
            for (id in listOf(27, 1034, 0, -1, null)) {
                val store = StartAlbumStoreFactory(DefaultStoreFactory(), repository, intentAction = action).create(emptyList(), id)
                try {
                    store.accept(StartAlbumStore.Intent.TogglePhoto(photos[0]))
                    store.accept(StartAlbumStore.Intent.ShareAlbum)
                    assertEquals(id, store.state.albumId)
                    assertEquals(setOf(photos[0].id), store.state.selectedPhotos.keys)
                    assertNull(store.state.exportProgress)
                } finally { store.dispose() }
            }
            assertEquals(listOf("https://sportsauce.ru/starts/photo/27", "https://sportsauce.ru/starts/photo/1034"), sent)
        } finally { Dispatchers.resetMain() }
    }

    @Test fun rangeSelectionCanReverseAndDeselectWithoutLosingUnrelatedPhotos() {
        val baseline = listOf(photos[0], photos[8]).associateBy { it.id }
        assertEquals(setOf(1, 3, 4, 5, 6, 9), albumSelectionRange(photos, baseline, 3, 6, true).keys)
        assertEquals(setOf(1, 3, 4, 9), albumSelectionRange(photos, baseline, 3, 4, true).keys)
        val selected = photos.associateBy { it.id }
        assertEquals(setOf(1, 2, 6, 7, 8, 9, 10), albumSelectionRange(photos, selected, 3, 5, false).keys)
    }

    @Test fun selectionSharesOriginalUrlsEvenWhenTheDisplayIsNotLoadedAndCanCancelOrRetry() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        var gate = CompletableDeferred<Unit>()
        var calls = 0; var sent = emptyList<String>()
        val sharer = object : OriginalImageSharer {
            override suspend fun share(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit) {
                calls++; sent = urls
                onProgress(OriginalImageShareProgress(0, urls.size, 512))
                gate.await()
            }
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo, sharer).create(emptyList())
        try {
            store.accept(StartAlbumStore.Intent.TogglePhoto(photos[0]))
            store.accept(StartAlbumStore.Intent.TogglePhoto(photos[1]))
            store.accept(StartAlbumStore.Intent.ShareSelection); runCurrent()
            assertEquals(photos.take(2).map { it.url }, sent)
            assertEquals(512, store.state.exportProgress!!.bytes)
            store.accept(StartAlbumStore.Intent.ShareSelection); assertEquals(1, calls)
            store.accept(StartAlbumStore.Intent.CancelExport); runCurrent()
            assertNull(store.state.exportProgress); assertEquals(2, store.state.selectedPhotos.size)
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.ShareSelection)
            gate.completeExceptionally(IllegalStateException("offline")); runCurrent()
            assertNotNull(store.state.exportError); assertEquals(2, store.state.selectedPhotos.size)
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.RetryExport); gate.complete(Unit); runCurrent()
            assertNull(store.state.exportProgress); assertNull(store.state.exportError); assertEquals(3, calls)
            store.accept(StartAlbumStore.Intent.GoBack); assertTrue(store.state.selectedPhotos.isEmpty())
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.SharePhotos(listOf(photos[8])))
            assertEquals(listOf(photos[8].url), sent)
            store.accept(StartAlbumStore.Intent.CancelExport)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun downloadsSelectionOrCurrentPhotoAndRetryKeepsDownloadActionAndSelection() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        var gate = CompletableDeferred<Unit>()
        var received = emptyList<String>()
        var calls = 0
        val downloader = object : OriginalImageDownloader {
            override suspend fun download(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit): OriginalImageDownloadResult {
                calls++; received = urls
                onProgress(OriginalImageShareProgress(0, urls.size, 256))
                gate.await()
                return OriginalImageDownloadResult(urls.size, "Галерея · Sportsauce")
            }
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo, downloader = downloader).create(emptyList())
        try {
            store.accept(StartAlbumStore.Intent.SelectPhotos(photos.take(2)))
            store.accept(StartAlbumStore.Intent.DownloadSelection); runCurrent()
            assertEquals(photos.take(2).map { it.url }, received)
            assertEquals(StartAlbumStore.ExportAction.Download, store.state.exportAction)
            store.accept(StartAlbumStore.Intent.ShareSelection)
            store.accept(StartAlbumStore.Intent.ClearSelection)
            assertEquals(2, store.state.selectedPhotos.size)
            assertEquals(1, calls)
            store.accept(StartAlbumStore.Intent.CancelExport); runCurrent()
            assertNull(store.state.downloadResult); assertNull(store.state.exportProgress)
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.DownloadSelection)
            gate.completeExceptionally(IllegalStateException("disk full")); runCurrent()
            assertNotNull(store.state.exportError); assertNull(store.state.downloadResult)
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.RetryExport); gate.complete(Unit); runCurrent()
            assertEquals(3, calls)
            assertEquals(OriginalImageDownloadResult(2, "Галерея · Sportsauce"), store.state.downloadResult)
            assertEquals(2, store.state.selectedPhotos.size)
            store.accept(StartAlbumStore.Intent.GoBack)
            assertNull(store.state.downloadResult)
            assertEquals(2, store.state.selectedPhotos.size)
            gate = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.DownloadPhotos(listOf(photos[8])))
            assertEquals(listOf(photos[8].url), received)
            gate.complete(Unit); runCurrent()
            assertEquals(1, store.state.downloadResult?.count)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }
}

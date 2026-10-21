package com.markettwits.sportsouce.start

import app.cash.paging.*
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartAlbumLoadingTest {
    private val album = StartAlbumDetails("Фотоальбом", "Трейл", "19 августа 2023", 791, listOf("ирина ткаченко", "саша j s-iron"))
    @Test fun remoteAlbumRetriesAfterFailureAndKeepsMetadataAndAllTags() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        var calls = 0
        var result = CompletableDeferred<StartAlbumDetails>()
        val repo = object : StartAlbumRepository {
            override suspend fun album(albumId: Int): StartAlbumDetails { assertEquals(9, albumId); calls++; return result.await() }
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>) = AlbumPhotosPage(emptyList(), 0)
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo).create(emptyList(), 9)
        try {
            assertTrue(store.state.isLoading)
            store.accept(StartAlbumStore.Intent.Retry); assertEquals(1, calls)
            result.completeExceptionally(IllegalStateException("offline")); runCurrent()
            assertFalse(store.state.isLoading); assertNotNull(store.state.error)
            result = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.Retry); assertTrue(store.state.isLoading); assertNull(store.state.error)
            result.complete(album); runCurrent()
            assertEquals(album, store.state.album); assertFalse(store.state.isLoading); assertNull(store.state.error)
            store.accept(StartAlbumStore.Intent.ToggleTag("unknown")); assertTrue(store.state.selectedTags.isEmpty())
            store.accept(StartAlbumStore.Intent.ToggleTag(album.tags[0]))
            store.accept(StartAlbumStore.Intent.ToggleTag(album.tags[1]))
            assertEquals(album.tags.toSet(), store.state.selectedTags)
            store.accept(StartAlbumStore.Intent.ToggleTag(album.tags[0])); assertEquals(setOf(album.tags[1]), store.state.selectedTags)
            store.accept(StartAlbumStore.Intent.ClearTags); assertTrue(store.state.selectedTags.isEmpty())
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun pagingPreservesOffsetAndSelectedTagsAndCanRetryAPage() = runTest {
        val calls = mutableListOf<Pair<Int, List<String>>>()
        var fail = true
        val repo = object : StartAlbumRepository {
            override suspend fun album(albumId: Int) = album
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage {
                assertEquals(9, albumId); assertEquals(30, limit); calls += offset to tags
                if (fail) error("offline")
                return AlbumPhotosPage(List(30) { AlbumPhoto(offset + it, "photo-${offset + it}") }, 229)
            }
        }
        val counts = mutableListOf<Int>()
        val source = AlbumPhotosPagingSource(repo, 9, album.tags, counts::add)
        val refresh = PagingSourceLoadParamsRefresh<Int>(null, 30, false)
        assertIs<PagingSourceLoadResultError<Int, AlbumPhoto>>(source.load(refresh))
        fail = false
        val page = assertIs<PagingSourceLoadResultPage<Int, AlbumPhoto>>(source.load(refresh))
        assertEquals(1, page.nextKey); assertEquals(0, page.data.first().id)
        val next = assertIs<PagingSourceLoadResultPage<Int, AlbumPhoto>>(source.load(PagingSourceLoadParamsAppend(1, 30, false)))
        assertEquals(30, next.data.first().id); assertEquals(2, next.nextKey)
        assertEquals(listOf(0 to album.tags, 0 to album.tags, 30 to album.tags), calls)
        assertEquals(listOf(229, 229), counts)
    }

    @Test fun existingStartAlbumWithImagesDoesNotRequestRemoteData() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = object : StartAlbumRepository {
            override suspend fun album(albumId: Int): StartAlbumDetails = error("Legacy album must use its images")
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage = error("Legacy album must use its images")
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo).create(listOf("image.jpg"))
        try {
            assertEquals(listOf("image.jpg"), store.state.images); assertFalse(store.state.isLoading)
            store.accept(StartAlbumStore.Intent.Retry)
            assertEquals(listOf("image.jpg"), store.state.images)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }
}

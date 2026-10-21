package com.markettwits.sportsouce.start

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumRepository
import com.markettwits.sportsouce.start.presentation.album.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartAlbumLoadingTest {
    @Test fun remoteAlbumRetriesAfterFailureAndKeepsEveryPhoto() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        var calls = 0
        var result = CompletableDeferred<List<String>>()
        val repo = object : StartAlbumRepository {
            override suspend fun images(albumId: Int): List<String> { assertEquals(27, albumId); calls++; return result.await() }
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo).create(emptyList(), 27)
        try {
            assertTrue(store.state.isLoading)
            store.accept(StartAlbumStore.Intent.Retry); assertEquals(1, calls)
            result.completeExceptionally(IllegalStateException("offline")); runCurrent()
            assertFalse(store.state.isLoading); assertNotNull(store.state.error)
            result = CompletableDeferred()
            store.accept(StartAlbumStore.Intent.Retry); assertTrue(store.state.isLoading); assertNull(store.state.error)
            result.complete(List(448) { "https://cdn.example.test/$it.jpg" }); runCurrent()
            assertEquals(448, store.state.images.size); assertFalse(store.state.isLoading); assertNull(store.state.error)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun existingStartAlbumWithImagesDoesNotRequestRemoteData() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = object : StartAlbumRepository {
            override suspend fun images(albumId: Int): List<String> = error("Existing album must use its images")
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repo).create(listOf("image.jpg"))
        try {
            assertEquals(listOf("image.jpg"), store.state.images); assertFalse(store.state.isLoading)
            store.accept(StartAlbumStore.Intent.Retry)
            assertEquals(listOf("image.jpg"), store.state.images)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }
}

package com.markettwits.sportsouce.review

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.IntentAction
import com.markettwits.sportsouce.review.review.domain.*
import com.markettwits.sportsouce.review.review.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewPhotoAlbumsStoreTest {
    @Test fun optionalAlbumsDoNotDelayReviewAndFailureCanRetryWithoutReloadingMainContent() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var mainLoads = 0
        var albumLoads = 0
        var fail = true
        val gate = CompletableDeferred<Unit>()
        val album = ReviewPhotoAlbum(27, "Юрманка трейл", "Фото", "17 октября 2026", 448, "")
        val repo = object : ReviewRepository {
            override suspend fun review(forced: Boolean): Flow<Review> { mainLoads++; return flowOf(Review()) }
        }
        val photos = object : ReviewPhotoAlbumsRepository {
            override fun albums() = flow {
                albumLoads++; gate.await()
                if (fail) error("offline")
                emit(listOf(album))
            }
        }
        val action = Proxy.newProxyInstance(IntentAction::class.java.classLoader, arrayOf(IntentAction::class.java)) { _, method, _ ->
            error("Unexpected ${method.name}")
        } as IntentAction
        val store = ReviewStoreFactory(DefaultStoreFactory(), repo, action, photos).create()
        try {
            store.accept(ReviewStore.Intent.Launch())
            val labels = mutableListOf<ReviewStore.Label>()
            store.labels.onEach { labels += it }.launchIn(backgroundScope)
            runCurrent()
            assertFalse(store.state.isLoading); assertTrue(store.state.photoAlbumsLoading)
            store.accept(ReviewStore.Intent.OnRetryPhotoAlbums); runCurrent(); assertEquals(1, albumLoads)
            gate.complete(Unit); runCurrent()
            assertNull(store.state.error); assertTrue(store.state.photoAlbumsFailed)
            fail = false
            store.accept(ReviewStore.Intent.OnRetryPhotoAlbums); runCurrent()
            assertEquals(1, mainLoads); assertEquals(2, albumLoads)
            assertFalse(store.state.photoAlbumsFailed); assertEquals(listOf(album), store.state.photoAlbums)
            store.accept(ReviewStore.Intent.OnClickPhotoAlbum(27))
            store.accept(ReviewStore.Intent.OnClickPhotoAlbum(999)); runCurrent()
            assertEquals(listOf<ReviewStore.Label>(ReviewStore.Label.OnClickPhotoAlbum(27)), labels)
            fail = true
            store.accept(ReviewStore.Intent.OnRetryPhotoAlbums); runCurrent()
            assertEquals(listOf(album), store.state.photoAlbums, "Keep cached covers when refresh fails")
            assertTrue(store.state.photoAlbumsFailed)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }
}

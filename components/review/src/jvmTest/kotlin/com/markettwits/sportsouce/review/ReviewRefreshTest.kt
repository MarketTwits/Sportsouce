package com.markettwits.sportsouce.review

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.*
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.IntentAction
import com.markettwits.sportsouce.review.review.domain.*
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponentBase
import com.markettwits.sportsouce.review.review.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewRefreshTest {
    @Test fun lifecycleRefreshesOnReturnAndKeepsCachedContentWithoutPullIndicator() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val loads = mutableListOf<Boolean>()
            var gate = CompletableDeferred<Unit>()
            val cached = Review(actualStarts = listOf(item(1)))
            val repo = object : ReviewRepository {
                override suspend fun review(forced: Boolean) = flow {
                    loads += forced
                    if (!forced) emit(cached)
                    gate.await()
                    emit(Review(actualStarts = listOf(item(2))))
                }
            }
            var albumsLoads = 0
            val albums = object : ReviewPhotoAlbumsRepository {
                override fun albums() = flow { albumsLoads++; emit(emptyList<ReviewPhotoAlbum>()) }
            }
            val component = ReviewComponentBase(DefaultComponentContext(lifecycle), factory(repo, albums), {}, {}, {}, {}, {}, {}, {})
            lifecycle.resume(); runCurrent()
            assertEquals(listOf(false), loads)
            assertFalse(component.value.value.isLoading)
            assertFalse(component.value.value.isRefreshing)
            assertTrue(component.value.value.hasLoaded, "Cached data must not be replaced by a loading screen")
            // A second open signal during the same network refresh must be coalesced.
            component.obtainEvent(ReviewStore.Intent.Launch()); runCurrent()
            assertEquals(1, loads.size)
            assertEquals(1, component.value.value.review.actualStarts.single().id)
            gate.complete(Unit); runCurrent()
            assertEquals(2, component.value.value.review.actualStarts.single().id)
            gate = CompletableDeferred()
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(listOf(false, false), loads)
            assertFalse(component.value.value.isRefreshing)
            assertFalse(component.value.value.isLoading)
            assertTrue(albumsLoads >= 2, "Optional albums must refresh on return too")
            component.obtainEvent(ReviewStore.Intent.Launch(true)); runCurrent()
            assertTrue(component.value.value.isRefreshing)
            assertFalse(component.value.value.isLoading)
            gate.complete(Unit); runCurrent()
            assertFalse(component.value.value.isRefreshing)
            assertEquals(listOf(false, false, true), loads)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test fun firstLoadShowsLoadingAndFailedBackgroundRefreshPreservesContent() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val gate = CompletableDeferred<Unit>()
        var fail = false
        val repo = object : ReviewRepository {
            override suspend fun review(forced: Boolean) = flow {
                gate.await()
                if (fail) error("offline")
                emit(Review())
            }
        }
        val store = factory(repo).create()
        try {
            store.accept(ReviewStore.Intent.Launch()); runCurrent()
            assertTrue(store.state.isLoading)
            assertFalse(store.state.isRefreshing)
            gate.complete(Unit); runCurrent()
            fail = true
            store.accept(ReviewStore.Intent.Launch()); runCurrent()
            assertFalse(store.state.isLoading)
            assertFalse(store.state.isRefreshing)
            assertNull(store.state.error)
            assertTrue(store.state.hasLoaded)
            store.accept(ReviewStore.Intent.Launch(true)); runCurrent()
            assertFalse(store.state.isRefreshing, "Failure must release the manual refresh indicator")
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    private fun item(id: Int) = StartsListItem(id, "Старт $id", "", "", StartsListItem.StatusCode(1, ""), "", "", "", false, "", views = 0)
    private fun factory(repo: ReviewRepository, albums: ReviewPhotoAlbumsRepository = object : ReviewPhotoAlbumsRepository {
        override fun albums() = flowOf(emptyList<ReviewPhotoAlbum>())
    }): ReviewStoreFactory {
        val action = Proxy.newProxyInstance(IntentAction::class.java.classLoader, arrayOf(IntentAction::class.java)) { _, method, _ ->
            error("Unexpected ${method.name}")
        } as IntentAction
        return ReviewStoreFactory(DefaultStoreFactory(), repo, action, albums)
    }
}

package com.markettwits.sportsouce.start

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import app.cash.paging.*
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStoreFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumPagingOverscrollTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun delayedInitialAndAppendLoadsNeverLatchTheGridAndOnlyTheFinalEdgeBounces() {
        val first = CompletableDeferred<Unit>()
        val second = CompletableDeferred<Unit>()
        val requested = mutableListOf<Int>()
        val repository = object : StartAlbumRepository {
            override suspend fun album(albumId: Int) = StartAlbumDetails("Альбом", "Трейл", "", 60, emptyList())
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage {
                requested += offset
                (if (offset == 0) first else second).await()
                return AlbumPhotosPage(if (offset < 60) List(30) { AlbumPhoto(offset + it, "") } else emptyList(), 60)
            }
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repository).create(emptyList(), 27)
        val states = store.stateFlow
        lateinit var photos: LazyPagingItems<AlbumPhoto>
        lateinit var grid: LazyGridState
        lateinit var bounce: AlbumOverscrollEffect
        lateinit var scope: CoroutineScope
        try {
            ImageComposeScene(width = 390, height = 900) {
                val model by states.collectAsState()
                SauceTheme {
                    photos = model.photos.collectAsLazyPagingItems()
                    grid = rememberLazyGridState(); bounce = remember { AlbumOverscrollEffect() }; scope = rememberCoroutineScope()
                    PagedAlbumContent(model, photos, {}, {}, gridState = grid, overscroll = bounce)
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun frames(count: Int = 12) { repeat(count) { time += 16_000_000L; scene.render(time).close() } }
                fun until(condition: () -> Boolean) {
                    repeat(100) { frames(1); if (condition()) return }
                    fail("Paging did not reach the expected state")
                }
                var point = Offset(195f, 300f)
                fun pointer(type: PointerEventType) {
                    time += 16_000_000L
                    scene.sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = time / 1_000_000)
                    frames(1)
                }
                fun drag(delta: Float) {
                    pointer(PointerEventType.Press)
                    repeat(10) { point += Offset(0f, delta / 10); pointer(PointerEventType.Move) }
                }
                until { photos.loadState.refresh is LoadStateLoading }
                drag(200f)
                assertEquals(0f, bounce.offset, "A loading skeleton is not an overscroll boundary")
                pointer(PointerEventType.Release); frames(120)
                first.complete(Unit)
                until { photos.itemCount == 30 && photos.loadState.append is LoadStateLoading }
                assertEquals(0, grid.firstVisibleItemIndex)
                assertTrue(30 in requested, "The next page must prefetch before the viewport reaches the last rows")
                scope.launch { grid.scrollToItem(grid.layoutInfo.totalItemsCount - 1) }
                until { !grid.canScrollForward }
                point = Offset(195f, 650f)
                drag(-250f)
                assertEquals(0f, bounce.offset, "The end of a pending page must not rubber-band")
                pointer(PointerEventType.Release); frames(120)
                val atFooter = grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset
                point = Offset(195f, 300f)
                drag(180f)
                assertNotEquals(atFooter, grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset,
                    "Reversing direction at a pending page must immediately move the grid")
                pointer(PointerEventType.Release); frames(120)
                scope.launch { grid.scrollToItem(grid.layoutInfo.totalItemsCount - 1) }
                until { !grid.canScrollForward }
                point = Offset(195f, 650f)
                drag(-100f)
                second.complete(Unit)
                until { photos.itemCount == 60 && (photos.loadState.append as? LoadStateNotLoading)?.endOfPaginationReached == true }
                assertEquals(0f, bounce.offset)
                assertTrue(grid.canScrollForward, "Appending must expose the new rows without jumping to the new end")
                val beforeContinue = grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset
                repeat(8) { point += Offset(0f, -20f); pointer(PointerEventType.Move) }
                assertNotEquals(beforeContinue, grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset)
                assertEquals(0f, bounce.offset)
                pointer(PointerEventType.Release); frames(120)
                scope.launch { grid.scrollToItem(grid.layoutInfo.totalItemsCount - 1) }
                until { !grid.canScrollForward }
                point = Offset(195f, 650f)
                drag(-150f)
                assertTrue(bounce.offset < 0f, "The actual final page retains the gallery bounce")
                pointer(PointerEventType.Release); frames(120)
                assertEquals(0f, bounce.offset, .1f)
                assertFalse(bounce.isInProgress)
            }
        } finally { store.dispose() }
    }
}

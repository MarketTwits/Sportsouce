package com.markettwits.sportsouce.start

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
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
class AlbumScrollbarInteractionTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun thumbSeeksUnloadedPagesAndReleasesBothEdgesWithoutLatching() = java.awt.EventQueue.invokeAndWait {
        val requests = mutableListOf<Int>()
        var failJump = true
        val pending = mutableListOf<CompletableDeferred<Unit>>()
        val repository = object : StartAlbumRepository {
            override suspend fun album(albumId: Int) = StartAlbumDetails("Альбом", "Трейл", "", 3000, emptyList())
            override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage {
                requests += offset
                CompletableDeferred<Unit>().also { pending += it }.await()
                if (offset > 1000 && failJump) { failJump = false; error("offline") }
                return AlbumPhotosPage(List((3000 - offset).coerceIn(0, limit)) { AlbumPhoto(offset + it, "") }, 3000)
            }
        }
        val store = StartAlbumStoreFactory(DefaultStoreFactory(), repository).create(emptyList(), 27)
        val states = store.stateFlow
        lateinit var grid: LazyGridState
        lateinit var bounce: AlbumOverscrollEffect
        try {
            ImageComposeScene(width = 390, height = 900) {
                val model by states.collectAsState()
                SauceTheme {
                    grid = rememberLazyGridState(); bounce = remember { AlbumOverscrollEffect() }
                    PagedAlbumContent(model, model.photos.collectAsLazyPagingItems(), {}, {}, gridState = grid, overscroll = bounce)
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun frames(count: Int = 12) { repeat(count) { time += 16_000_000L; scene.render(time).close(); pending.toList().also { pending.clear() }.forEach { it.complete(Unit) } } }
                fun until(message: String, condition: () -> Boolean) {
                    repeat(160) { frames(1); if (condition()) return }
                    fail("$message; grid=${grid.firstVisibleItemIndex}:${grid.firstVisibleItemScrollOffset}, forward=${grid.canScrollForward}, viewport=${grid.layoutInfo.viewportStartOffset}..${grid.layoutInfo.viewportEndOffset}, visible=${grid.layoutInfo.visibleItemsInfo.map { Triple(it.index, it.offset.y, it.size.height) }}, bounce=${bounce.offset}, requests=$requests")
                }
                fun nodes(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::nodes)
                fun thumb() = scene.semanticsOwners.flatMap { nodes(it.unmergedRootSemanticsNode) }
                    .first { "Прокрутка фотоальбома" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
                fun pointer(type: PointerEventType, point: Offset) {
                    time += 16_000_000L
                    scene.sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = time / 1_000_000)
                    frames(2)
                }
                until("Initial page") { grid.layoutInfo.totalItemsCount > 3000 }
                frames()
                val first = thumb().boundsInRoot.center
                pointer(PointerEventType.Press, first)
                pointer(PointerEventType.Move, first + Offset(0f, 380f))
                pointer(PointerEventType.Release, first + Offset(0f, 380f))
                until("Jump must load near the new viewport") { grid.firstVisibleItemIndex > 1000 && requests.any { it > 1000 } }
                fun retryButton(): SemanticsNode? = scene.semanticsOwners.flatMap { nodes(it.unmergedRootSemanticsNode) }
                    .firstOrNull { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Повторить" } }
                until("A failed distant page must expose retry in the current viewport") { retryButton() != null }
                var retry = checkNotNull(retryButton())
                while (!retry.config.contains(SemanticsActions.OnClick)) retry = checkNotNull(retry.parent)
                retry.config[SemanticsActions.OnClick].action!!.invoke()
                until("Retry restores the page") { retryButton() == null && requests.count { it > 1000 } > 1 }
                assertTrue(requests.size < 15, "A jump must not fetch every preceding page: $requests")
                assertEquals(0f, bounce.offset, .1f)
                frames()
                val middle = thumb().boundsInRoot.center
                pointer(PointerEventType.Press, middle)
                pointer(PointerEventType.Move, middle + Offset(0f, 700f))
                until("Final edge") { !grid.canScrollForward }
                until("Thumb drag must use the gallery's bottom resistance") { bounce.offset < 0f }
                pointer(PointerEventType.Release, middle + Offset(0f, 700f)); frames(120)
                assertEquals(0f, bounce.offset, .1f)
                val bottom = thumb().boundsInRoot.center
                pointer(PointerEventType.Press, bottom)
                pointer(PointerEventType.Move, bottom - Offset(0f, 1000f))
                until("Return to first edge") { !grid.canScrollBackward }
                pointer(PointerEventType.Release, bottom - Offset(0f, 1000f)); frames(120)
                assertEquals(0f, bounce.offset, .1f)
                assertFalse(bounce.isInProgress)
                // Ordinary photo-area scrolling must still work after a complete thumb round trip.
                pointer(PointerEventType.Press, Offset(195f, 650f))
                repeat(10) { pointer(PointerEventType.Move, Offset(195f, 650f - (it + 1) * 30f)) }
                pointer(PointerEventType.Release, Offset(195f, 350f)); frames(30)
                assertTrue(grid.canScrollBackward)
            }
        } finally { store.dispose() }
    }
}

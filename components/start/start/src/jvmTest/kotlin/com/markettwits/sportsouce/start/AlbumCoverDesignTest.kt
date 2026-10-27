package com.markettwits.sportsouce.start

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import app.cash.paging.*
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumCoverDesignTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun coverCollapsesContinuouslyPinsTagsAndRestoresOnScrollBack() {
        for (dark in listOf(false, true)) for ((width, scale) in listOf(390 to 1f, 320 to 2f, 800 to 1f)) {
            val album = StartAlbumDetails("Домашний ультра трейл", "Большой спортивный праздник", "19 августа 2023", 90,
                listOf("ирина ткаченко", "саша j s-iron", "настя маметьева"))
            val pages = flowOf(PagingData.from(List(90) { AlbumPhoto(it, "") },
                sourceLoadStates = LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true))))
            var model by mutableStateOf(StartAlbumStore.State(album = album, albumId = 27, photos = pages, filteredCount = 90))
            lateinit var grid: LazyGridState
            lateinit var scope: CoroutineScope
            var back = 0
            ImageComposeScene(width = width, height = 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    grid = rememberLazyGridState()
                    scope = rememberCoroutineScope()
                    PagedAlbumContent(model, model.photos.collectAsLazyPagingItems(), {}, {}, gridState = grid,
                        onEvent = { if (it == StartAlbumStore.Intent.GoBack) back++ })
                }
            }.use { scene ->
                scene.settle()
                fun text(label: String) = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == label } }
                    .minBy { it.boundsInRoot.top }
                fun clickParent(node: SemanticsNode): SemanticsNode = if (node.config.contains(SemanticsActions.OnClick)) node else clickParent(node.parent!!)
                val expandedTags = clickParent(text("Все фото")).boundsInRoot
                val extent = (grid.layoutInfo.visibleItemsInfo.first { it.key == "cover" }.size.height).toFloat()
                assertTrue(expandedTags.top > 400f)
                if (scale == 1f && width == 390) scene.save("album-cover-${if (dark) "dark" else "light"}-expanded")
                for (fraction in listOf(.25f, .55f, .75f, 1f)) {
                    scope.launch { grid.scrollToItem(0, (extent * fraction).toInt()) }; scene.settle()
                    val tags = clickParent(text("Все фото")).boundsInRoot
                    assertEquals(expandedTags.top - minOf(extent * fraction, extent), tags.top, 2f)
                    assertEquals(fraction, albumCoverScrollOffset(grid, extent) / extent, .01f)
                    if (fraction == .75f && scale == 1f && width == 390) scene.save("album-cover-${if (dark) "dark" else "light"}-transition")
                }
                scope.launch { grid.scrollToItem(30) }; scene.settle()
                val pinnedTags = clickParent(text("Все фото")).boundsInRoot
                assertTrue(pinnedTags.top >= 56f)
                assertTrue(pinnedTags.bottom <= 56f + 100f * scale)
                assertTrue(text(album.name).boundsInRoot.bottom <= pinnedTags.top)
                if (scale == 1f && width == 390) scene.save("album-cover-${if (dark) "dark" else "light"}-collapsed")
                // Selecting tags resets only the photos, leaving the header compact.
                model = model.copy(selectedTags = setOf(album.tags.first()))
                scene.settle()
                assertEquals(pinnedTags.top, clickParent(text("Все фото")).boundsInRoot.top, .5f)
                assertEquals(1f, albumCoverScrollOffset(grid, extent) / extent, .01f)
                val pinnedHeight = (pinnedTags.height + 16f + 56f).toInt()
                scope.launch { grid.revealAlbumPhoto(73, pinnedHeight) }; scene.settle()
                val returnedPhoto = grid.layoutInfo.visibleItemsInfo.first { it.index == 73 }
                assertTrue(returnedPhoto.offset.y >= grid.layoutInfo.viewportStartOffset + pinnedHeight, "Returned photo must be below pinned filters")
                model = model.copy(photos = flowOf(PagingData.from(emptyList(),
                    sourceLoadStates = LoadStates(LoadStateLoading, LoadStateNotLoading(true), LoadStateNotLoading(true)))))
                scene.settle()
                assertEquals(1f, albumCoverScrollOffset(grid, extent) / extent, .01f, "Loading a filter must not reopen the cover")
                assertEquals(pinnedTags.top, clickParent(text("Все фото")).boundsInRoot.top, .5f)
                model = model.copy(photos = flowOf(PagingData.from(emptyList(),
                    sourceLoadStates = LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true)))), filteredCount = 0)
                scene.settle()
                if (scale == 1f && width == 390) scene.save("album-cover-${if (dark) "dark" else "light"}-empty")
                assertEquals(1f, albumCoverScrollOffset(grid, extent) / extent, .01f, "Empty filters must not reopen the cover")
                assertEquals(pinnedTags.top, clickParent(text("Все фото")).boundsInRoot.top, .5f,
                    "Empty viewport: ${grid.firstVisibleItemIndex}:${grid.firstVisibleItemScrollOffset} ${grid.layoutInfo.visibleItemsInfo.map { Triple(it.key, it.offset.y, it.size.height) }}")
                scope.launch { grid.scrollToItem(0) }; scene.settle()
                assertEquals(0f, albumCoverScrollOffset(grid, extent))
                assertEquals(expandedTags.top, clickParent(text("Все фото")).boundsInRoot.top, .5f)
                val backIcon = scene.nodes().first { "Назад" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
                clickParent(backIcon).config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, back)
            }
        }
    }

    @Test fun albumsWithoutServerTagsStillKeepTheFilterStripWhenCollapsed() {
        for (dark in listOf(false, true)) {
            val album = StartAlbumDetails("Фото", "Юрманка трейл", "17 октября 2026", 448, emptyList())
            val pages = flowOf(PagingData.from(List(60) { AlbumPhoto(it, "") },
                sourceLoadStates = LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true))))
            lateinit var grid: LazyGridState
            lateinit var scope: CoroutineScope
            ImageComposeScene(width = 390, height = 900) {
                SauceTheme(darkTheme = dark) {
                    grid = rememberLazyGridState(); scope = rememberCoroutineScope()
                    PagedAlbumContent(StartAlbumStore.State(album = album, photos = pages, filteredCount = 448),
                        pages.collectAsLazyPagingItems(), {}, {}, gridState = grid)
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Все фото" } })
                scope.launch { grid.scrollToItem(20) }; scene.settle()
                val chip = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Все фото" } }
                var filter = chip
                while (!filter.config.contains(SemanticsActions.OnClick)) filter = filter.parent!!
                assertEquals(64f, filter.boundsInRoot.top, .5f)
                assertTrue(filter.boundsInRoot.height >= 48f)
                scene.save("album-cover-${if (dark) "dark" else "light"}-no-tags-collapsed")
            }
        }
    }

    @Test fun draggingTheCoverStretchesItAndSpringsBackWithFixedToolbarAndWorkingShareAction() {
        val album = StartAlbumDetails("Фото", "Юрманка трейл", "17 октября 2026", 60, listOf("Фотограф"))
        val pages = flowOf(PagingData.from(List(60) { AlbumPhoto(it, "") },
            sourceLoadStates = LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true))))
        lateinit var grid: LazyGridState
        lateinit var bounce: AlbumOverscrollEffect
        var shares = 0
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme(darkTheme = true) {
                grid = rememberLazyGridState(); bounce = remember { AlbumOverscrollEffect() }
                PagedAlbumContent(StartAlbumStore.State(album = album, albumId = 27, photos = pages, filteredCount = 60),
                    pages.collectAsLazyPagingItems(), {}, {}, gridState = grid, overscroll = bounce,
                    onEvent = { if (it == StartAlbumStore.Intent.ShareAlbum) shares++ })
            }
        }.use { scene ->
            scene.settle()
            fun filter() = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Все фото" } }
            fun icon(label: String) = scene.nodes().first { label in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
            fun button(node: SemanticsNode): SemanticsNode = if (node.config.contains(SemanticsActions.OnClick)) node else button(node.parent!!)
            val back = button(icon("Назад")).boundsInRoot
            val filterTop = filter().boundsInRoot.top
            var point = Offset(195f, 180f)
            fun pointer(type: PointerEventType) {
                frameTime += 16_000_000L
                scene.sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = frameTime / 1_000_000)
                scene.render(frameTime).close()
            }
            pointer(PointerEventType.Press)
            repeat(10) { point += Offset(0f, 20f); pointer(PointerEventType.Move) }
            repeat(3) { frameTime += 16_000_000L; scene.render(frameTime).close() }
            assertTrue(bounce.offset > 20f, "Dragging down at the first row must stretch the cover")
            assertEquals(0, grid.firstVisibleItemIndex)
            assertEquals(0, grid.firstVisibleItemScrollOffset)
            assertTrue(filter().boundsInRoot.top > filterTop + 20f)
            assertEquals(back, button(icon("Назад")).boundsInRoot, "Toolbar must stay fixed during overscroll")
            scene.save("album-cover-overscroll-held")
            pointer(PointerEventType.Release)
            repeat(120) { frameTime += 16_000_000L; scene.render(frameTime).close() }
            assertEquals(0f, bounce.offset, .1f)
            assertEquals(filterTop, filter().boundsInRoot.top, 1f)
            val share = button(icon("Поделиться альбомом"))
            assertTrue(share.boundsInRoot.width >= 48f)
            assertTrue(share.boundsInRoot.right <= 390f)
            share.config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(1, shares)
        }
    }

    private var frameTime = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { frameTime += 100_000_000L; render(frameTime).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.save(name: String) {
        frameTime += 100_000_000L
        render(frameTime).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}

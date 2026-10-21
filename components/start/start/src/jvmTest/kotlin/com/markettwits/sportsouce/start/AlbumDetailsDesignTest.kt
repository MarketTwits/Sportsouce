package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import app.cash.paging.*
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.album.domain.*
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumDetailsDesignTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private val album = StartAlbumDetails("Фотоальбом", "Домашний ультра кросс-трейл", "19 августа 2023", 791,
        listOf("ирина ткаченко", "саша j s-iron", "настя маметьева"))

    @Test fun tagsAndResponsivePhotoGridFitBothThemesAndLargeText() {
        for ((width, scale) in listOf(320 to 2f, 390 to 1f, 800 to 1f)) for (dark in listOf(false, true)) {
            var tag: String? = null
            var clears = 0
            val flow = flowOf(PagingData.from(List(6) { AlbumPhoto(it, "") },
                sourceLoadStates = LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true))))
            ImageComposeScene(width = width, height = 1200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        AlbumTagFilters(album.tags, emptySet(), { tag = it }, { clears++ })
                        PagedAlbumContent(StartAlbumStore.State(album = album, filteredCount = 791), flow.collectAsLazyPagingItems(), {}, {})
                    }
                }
            }.use { scene ->
                repeat(16) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                fun text(s: String) = nodes.first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == s } }
                fun button(n: SemanticsNode): SemanticsNode = if (n.config.contains(SemanticsActions.OnClick)) n else button(checkNotNull(n.parent))
                button(text("Все фото")).config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(1, clears)
                button(text(album.tags[0])).config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(album.tags[0], tag)
                val photos = nodes.filter { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().any { d -> d.matches(Regex("Фото [0-9]+")) } }
                val columns = if (width < 600) 3 else width / 160
                assertTrue(photos.size >= columns)
                for (column in 1 until columns) {
                    assertEquals(photos[0].boundsInRoot.top, photos[column].boundsInRoot.top)
                    assertEquals(1f, photos[column].boundsInRoot.left - photos[column - 1].boundsInRoot.right, .1f)
                }
                assertEquals(0f, photos[0].boundsInRoot.left)
                assertEquals(width.toFloat(), photos[columns - 1].boundsInRoot.right)
                nodes.forEach { n ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                        assertFalse(it.didOverflowHeight, "Clipped text: ${it.layoutInput.text}")
                    }
                }
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/album-details-${if (dark) "dark" else "light"}-$width.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
    @Test fun returningFromViewerRevealsTheLastPhotoAndKeepsAlreadyVisibleRows() {
        lateinit var grid: LazyGridState
        lateinit var scope: CoroutineScope
        ImageComposeScene(width = 390, height = 500) {
            SauceTheme {
                grid = rememberLazyGridState()
                scope = rememberCoroutineScope()
                StartAlbumPhotoGrid(List(80) { "" }, grid, {})
            }
        }.use { scene ->
            fun settle() { repeat(16) { scene.render(System.nanoTime() + it * 100_000_000L).close() } }
            settle()
            scope.launch { grid.revealAlbumPhoto(60) }; settle()
            assertTrue(grid.layoutInfo.visibleItemsInfo.any { it.index == 60 })
            val position = grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset
            scope.launch { grid.revealAlbumPhoto(61) }; settle()
            assertEquals(position, grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset)
        }
    }
    @Test fun selectionActionsFitBothThemesAndLargeTextWithoutOverlapping() {
        for ((width, scale) in listOf(320 to 2f, 390 to 1f)) for (dark in listOf(false, true)) {
            var downloads = 0; var shares = 0
            ImageComposeScene(width = width, height = 180, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    AlbumSelectionPanel(12, {}, { shares++ }, { downloads++ })
                }
            }.use { scene ->
                repeat(8) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                val download = nodes.first { "Скачать выбранные фото" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
                var button = download
                while (!button.config.contains(SemanticsActions.OnClick)) button = checkNotNull(button.parent)
                assertTrue(button.boundsInRoot.width >= 48f)
                button.config[SemanticsActions.OnClick].action!!.invoke()
                val share = nodes.first { "Поделиться выбранными фото" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() ||
                    it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Поделиться" } }
                button = share
                while (!button.config.contains(SemanticsActions.OnClick)) button = checkNotNull(button.parent)
                button.config[SemanticsActions.OnClick].action!!.invoke()
                assertTrue(button.boundsInRoot.left >= download.boundsInRoot.right)
                assertTrue(button.boundsInRoot.right <= width)
                assertEquals(1, downloads); assertEquals(1, shares)
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/album-actions-${if (dark) "dark" else "light"}-$width.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
    private fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
}

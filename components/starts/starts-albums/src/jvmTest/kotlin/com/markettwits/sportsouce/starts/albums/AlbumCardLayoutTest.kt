package com.markettwits.sportsouce.starts.albums

import app.cash.paging.*
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import com.markettwits.sportsouce.starts.albums.presentation.screen.StartsAlbumsContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.albums.domain.StartAlbumsItem
import com.markettwits.sportsouce.starts.albums.presentation.components.StartAlbumCard
import com.markettwits.sportsouce.starts.albums.presentation.components.AlbumsSearchBar
import com.markettwits.sportsouce.starts.albums.filter.screen.AlbumsFilterContent
import com.markettwits.sportsouce.starts.albums.filter.store.AlbumsFilterStore
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumCardLayoutTest {
    @BeforeTest fun installMainDispatcher() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMainDispatcher() { Dispatchers.resetMain() }
    @Test fun archiveFiltersAndAlbumCardFitSmallScreensInBothThemes() {
        for ((width, scale) in listOf(390 to 1f, 320 to 1.5f)) for (dark in listOf(false, true)) {
            var opened = false
            ImageComposeScene(width = width, height = 1250, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        AlbumsSearchBar("", false, {}, {}, {})
                        Box(Modifier.padding(16.dp)) {
                            StartAlbumCard(StartAlbumsItem.Album(27, "Фотографии с трейла — большой альбом участников", 448, "")) { opened = true }
                        }
                    }
                }
            }.use { scene ->
                repeat(8) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                val cardTitle = nodes.first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text.startsWith("Фотографии с трейла") } }
                var card = cardTitle
                while (!card.config.contains(SemanticsActions.OnClick)) card = checkNotNull(card.parent)
                card.config[SemanticsActions.OnClick].action!!.invoke(); assertTrue(opened)
                assertTrue(card.size.height >= 48)
                nodes.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                        assertFalse(it.didOverflowHeight, "Clipped text: ${it.layoutInput.text}")
                    }
                }
                scene.render(System.nanoTime() + 1_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/albums-${if (dark) "dark" else "light"}-$width.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
    @Test fun filterSheetActionsFitLargeTextInBothThemes() {
        for (dark in listOf(false, true)) {
            val events = mutableListOf<AlbumsFilterStore.Intent>()
            ImageComposeScene(width = 320, height = 900, density = Density(1f, 1.5f)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.surface)) {
                        AlbumsFilterContent(2025) { events += it }
                    }
                }
            }.use { scene ->
                repeat(8) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                for (label in listOf("Сбросить", "Применить")) {
                    var node = nodes.first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == label } }
                    while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
                    assertTrue(node.size.height >= 48)
                    node.config[SemanticsActions.OnClick].action!!.invoke()
                }
                assertEquals(listOf(AlbumsFilterStore.Intent.Reset, AlbumsFilterStore.Intent.Apply), events)
                nodes.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { assertFalse(it.didOverflowHeight, "Clipped filter text: ${it.layoutInput.text}") }
                }
            }
        }
    }
    @Test fun albumsUseOneColumnOnPhoneAndMultipleColumnsOnWideScreens() {
        val albums = listOf(
            StartAlbumsItem.Album(1, "На старте", 10, ""),
            StartAlbumsItem.Album(2, "На финише", 20, ""),
        )
        for (width in listOf(320, 800)) for (dark in listOf(false, true)) {
            var selected: StartAlbumsItem.Album? = null
            val flow = flowOf(PagingData.from(listOf(StartAlbumsItem(1, "Кросс", "15 октября 2026", albums)),
                sourceLoadStates = LoadStates(LoadStateNotLoading(false), LoadStateNotLoading(false), LoadStateNotLoading(false))))
            ImageComposeScene(width = width, height = 1200, density = Density(1f)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        StartsAlbumsContent(flow.collectAsLazyPagingItems()) { selected = it }
                    }
                }
            }.use { scene ->
                repeat(14) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                fun text(value: String) = nodes.first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == value } }
                val first = text("На старте")
                val second = text("На финише")
                if (width == 320) {
                    assertEquals(first.boundsInRoot.left, second.boundsInRoot.left)
                    assertTrue(second.boundsInRoot.top > first.boundsInRoot.bottom)
                } else {
                    assertEquals(first.boundsInRoot.top, second.boundsInRoot.top)
                    assertTrue(second.boundsInRoot.left > first.boundsInRoot.right)
                }
                val count = text("10 фото")
                assertTrue(count.boundsInRoot.top - first.boundsInRoot.bottom in 0f..12f,
                    "Short album titles must not reserve an empty second line")
                var card = second
                while (!card.config.contains(SemanticsActions.OnClick)) card = checkNotNull(card.parent)
                card.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(albums[1], selected)
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/albums-grid-${if (dark) "dark" else "light"}-$width.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
    private fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
}

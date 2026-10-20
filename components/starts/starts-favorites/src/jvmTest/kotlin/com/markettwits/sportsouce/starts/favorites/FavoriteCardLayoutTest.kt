package com.markettwits.sportsouce.starts.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.presentation.components.FavoriteStartCard
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class FavoriteCardLayoutTest {
    @Test
    fun favoriteRemoveLoadingAndRestoreKeepGeometryAndDoNotOpenTheCard() {
        var loading by mutableStateOf(false)
        var removed by mutableStateOf(false)
        var opens = 0
        var removes = 0
        var adds = 0
        ImageComposeScene(width = 390, height = 800) {
            SauceTheme {
                Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                    FavoriteStartCard(start = start(), isDeleting = loading, isRemoved = removed,
                        onItemClick = { opens++ }, onFavoriteClick = { removes++ }, onAddToFavorites = { adds++ })
                }
            }
        }.use { scene ->
            scene.settle()
            val first = scene.action("Убрать из избранного").boundsInRoot
            assertTrue(first.width >= 48f && first.height >= 48f)
            scene.tap(scene.action("Убрать из избранного")); scene.settle()
            assertEquals(1, removes); assertEquals(0, opens)
            loading = true; scene.settle()
            assertEquals(first, scene.action("Обновление избранного").boundsInRoot)
            scene.tap(scene.action("Обновление избранного")); scene.settle()
            assertEquals(1, removes); assertEquals(0, opens, "Disabled favorite control must absorb taps")
            loading = false; removed = true; scene.settle()
            assertEquals(first, scene.action("Добавить в избранное").boundsInRoot)
            scene.tap(scene.action("Добавить в избранное")); scene.settle()
            assertEquals(1, adds); assertEquals(0, opens)
            scene.tap(scene.text(start().name)); scene.settle()
            assertEquals(1, opens)
        }
    }

    @Test
    fun compactCardsFitLongContentAndLargeTextInBothThemes() {
        for (dark in listOf(false, true)) for (width in listOf(320, 390, 600)) for (scale in listOf(1f, 2f)) {
            val item = start().copy(name = "Открытие зимнего спортивного сезона Центрального округа Новосибирска",
                kindOfSports = listOf(StartsListItem.KindOfSport(1, "Лыжные гонки")),
                place = "Академгородок, лыжная база имени Алика Тульского", distance = "<b>5 км</b>, детский забег")
            ImageComposeScene(width = width, height = 1300, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        FavoriteStartCard(start = item, onItemClick = {}, onFavoriteClick = {}, onAddToFavorites = {})
                    }
                }
            }.use { scene ->
                scene.settle()
                val card = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
                if (scale == 1f) assertTrue(card.boundsInRoot.height <= 280f, "Card should remain compact: ${card.boundsInRoot}")
                assertTrue(card.boundsInRoot.right <= width - 16f)
                scene.text(item.name); scene.text(item.date); scene.text(item.place); scene.text("Лыжные гонки")
                scene.assertTextFits()
                if (width == 390 && scale == 1f) scene.save("favorite-${if (dark) "dark" else "light"}")
                if (width == 320 && scale == 2f && !dark) scene.save("favorite-large-text")
            }
        }
    }

    @Test
    fun partialStartDataDoesNotLeaveEmptyMetadataRows() {
        ImageComposeScene(width = 320, height = 700) {
            SauceTheme {
                Box(Modifier.padding(16.dp)) {
                    FavoriteStartCard(start = start().copy(place = "", distance = "", date = "", kindOfSports = emptyList()),
                        onItemClick = {}, onFavoriteClick = {}, onAddToFavorites = {})
                }
            }
        }.use { scene -> scene.settle(); scene.assertTextFits(); scene.text(start().name) }
    }

    private fun start() = StartsListItem(315, "Страшно добрый забег", "", "25 октября 2026", StartsListItem.StatusCode(3, ""),
        "", "albatros", "Новосибирск, Заельцовский парк", false, "4 км, детский забег",
        kindOfSports = listOf(StartsListItem.KindOfSport(1, "Бег")), views = 0)
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.action(description: String) = nodes().single {
        it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(description) == true
    }
    private fun ImageComposeScene.text(text: String) = nodes().first {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == text } == true
    }
    private fun ImageComposeScene.tap(n: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, n.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, n.boundsInRoot.center)
    }
    private fun ImageComposeScene.assertTextFits() {
        for (node in nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
            for (layout in layouts) {
                if (layout.layoutInput.overflow != TextOverflow.Ellipsis) {
                    assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Text too tall: ${layout.layoutInput.text}")
                    for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f,
                        "Text too wide: ${layout.layoutInput.text}")
                }
                assertTrue(node.boundsInRoot.width + 1f >= layout.size.width, "Text clipped: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}

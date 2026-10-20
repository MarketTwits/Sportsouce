package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.start.presentation.series.components.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartSeriesDesignTest {
    private fun item(id: Int, year: Int) = StartsListItem(id, "Страшно добрый забег — осенняя серия стартов", "",
        "25 октября $year", StartsListItem.StatusCode(if (year < 2026) 6 else 3, ""), "", "start-$id",
        "Новосибирск, Заельцовский парк", false, "Дистанция: 4 км<br>Детский забег",
        listOf(StartsListItem.KindOfSport(1, "бег")), views = 12345)
    private val items = listOf(item(1, 2024), item(2, 2025), item(3, 2026))

    @Test
    fun cardsKeepTheirOutlineSelectionAndReadableContentInBothThemesAndAt200Percent() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 1600, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)
                        .verticalScroll(rememberScrollState()).padding(16.dp)) {
                        SeriesStartCard(item = items[1], onClick = {})
                        StartSeriesConnector(toCurrentStart = true)
                        SeriesStartCard(item = items[2], isCurrentStart = true, onClick = {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                val cards = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
                assertEquals(2, cards.size)
                if (scale == 1f) cards.forEach { assertTrue(it.size.height < 400,
                    "A poster must not stretch a card to the viewport height") }
                assertFalse(cards[0].config[SemanticsProperties.Selected])
                assertTrue(cards[1].config[SemanticsProperties.Selected])
                assertEquals("Основной старт", cards[1].config[SemanticsProperties.StateDescription])
                assertEquals(40f, cards[1].positionInRoot.y - cards[0].positionInRoot.y - cards[0].size.height, 0.5f,
                    "Connector must occupy only the gap between cards")
                val palette = sauceColors(dark)
                time += 100_000_000L
                scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    val y = (cards[0].positionInRoot.y + cards[0].size.height / 2).toInt()
                    assertEquals(palette.controlBorder.toArgb(), bitmap.getRGB(16, y), "Ordinary card must have a visible outline")
                    val currentY = (cards[1].positionInRoot.y + cards[1].size.height / 2).toInt()
                    if (currentY < bitmap.height) assertEquals(palette.brand.toArgb(), bitmap.getRGB(16, currentY))
                } }
                assertTrue(scene.texts().contains("12345"))
                assertTrue(scene.texts().contains(items[1].place))
                scene.save("start-series-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun historyConnectorHasGapsAndTheMainStartConnectorIsContinuous() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(width = 100, height = 80) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.background(SauceTheme.colors.background)) {
                        StartSeriesConnector(toCurrentStart = false)
                        StartSeriesConnector(toCurrentStart = true)
                    }
                }
            }.use { scene ->
                scene.settle(); time += 100_000_000L
                scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    val palette = sauceColors(dark)
                    val history = (0 until 40).map { bitmap.getRGB(50, it) }
                    assertTrue(history.contains(palette.background.toArgb()), "Historical line must contain visible gaps")
                    assertTrue(history.contains(palette.controlBorder.toArgb()), "Historical line must contain visible dashes")
                    for (y in 40 until 80) assertEquals(palette.brand.toArgb(), bitmap.getRGB(50, y), "Main start line must be solid")
                } }
            }
        }
    }

    @Test
    fun timelineKeepsTheOriginalOrderAndNavigatesToTheSelectedRecordOrBackToCurrentStart() {
        var backs = 0
        val opened = mutableListOf<StartsListItem>()
        ImageComposeScene(width = 390, height = 1500) {
            SauceTheme { StartSeriesContent(items, 3, { backs++ }, { opened += it }) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            val cards = items.map { event -> scene.nodes().single { node -> node.config.contains(SemanticsActions.OnClick) &&
                node.config.getOrNull(SemanticsProperties.Text)?.any { it.text == event.date } == true } }
            assertTrue(cards.zipWithNext().all { (left, right) -> left.positionInRoot.y < right.positionInRoot.y })
            cards[1].config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertEquals(listOf(items[1]), opened)
            cards[2].config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertEquals(1, backs); assertEquals(1, opened.size)
            val back = scene.nodes().single { node -> node.config.contains(SemanticsActions.OnClick) &&
                node.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Назад") == true }
            back.config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(2, backs)
            scene.save("start-series-screen")
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(unmerged: Boolean = false): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(if (unmerged) it.unmergedRootSemanticsNode else it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { t -> t.text } }
    private fun ImageComposeScene.assertTextFits() {
        nodes(true).forEach { n ->
            val layouts = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { if (it.layoutInput.maxLines == Int.MAX_VALUE) assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}") }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}

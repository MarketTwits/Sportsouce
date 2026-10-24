package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.start.presentation.series.StartRelatedKind
import com.markettwits.sportsouce.start.presentation.series.components.StartSeriesContent
import com.markettwits.sportsouce.start.presentation.start.components.StartArchivePanel
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import java.io.File
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartArchiveDesignTest {
    private val items = (2022..2026).map { year -> StartsListItem(year, "Страшно добрый забег", "", "25 октября $year",
        StartsListItem.StatusCode(if (year == 2026) 3 else 6, ""), "", "", "Новосибирск, Заельцовский парк", false,
        "4 км · Детский забег", kindOfSports = listOf(StartsListItem.KindOfSport(1, "Бег")), views = 125,
        startDate = "$year-10-25T10:00:00Z") }

    @Test fun archivePreviewShowsRecentPastEditionsAndExpandsForLargeText() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var opens = 0
            val selected = mutableListOf<Int>()
            ImageComposeScene(width = 320, height = 1800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState())) {
                    StartArchivePanel(Modifier.padding(16.dp), items, 2026, false, false, { opens++ }, {}, { selected += it.id })
                } }
            }.use { scene ->
                scene.settle()
                for (year in listOf(2023, 2024, 2025)) assertTrue(scene.texts().contains("25 октября $year"))
                assertFalse(scene.texts().contains("25 октября 2022"))
                assertFalse(scene.texts().contains("25 октября 2026"))
                scene.assertTextFits()
                val action = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) && it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Архив стартов" } == true }
                action.config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(1, opens)
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) && it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "25 октября 2024" } == true }
                card.config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(listOf(2024), selected)
                if (scale == 1f) scene.save("archive-preview-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test fun timelineHasYearsAndCurrentEditionReturnsToDetails() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var backs = 0
            val opened = mutableListOf<Int>()
            ImageComposeScene(width = 390, height = 1800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { StartSeriesContent(items, 2026, { backs++ }, { opened += it.id }, StartRelatedKind.Archive) }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.texts().contains("Архив стартов")); assertTrue(scene.texts().contains("2022"))
                val first = scene.card("25 октября 2022")
                first.config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(listOf(2022), opened)
                val scroll = scene.nodes().single { it.config.contains(SemanticsActions.ScrollToIndex) }
                scroll.config[SemanticsActions.ScrollToIndex].action!!.invoke(4); scene.settle()
                assertTrue(scene.texts().contains("2026")); scene.assertTextFits()
                val current = scene.card("25 октября 2026")
                assertTrue(current.config[SemanticsProperties.Selected])
                val previous = scene.card("25 октября 2025")
                val from = ceil(previous.boundsInRoot.bottom).toInt()
                val until = floor(current.boundsInRoot.top).toInt()
                assertTrue(until - from > 40, "Gap includes the year label as well as spacing")
                time += 100_000_000L
                scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    for (y in from until until) {
                        assertEquals(sauceColors(dark).brand.toArgb(), bitmap.getRGB(195, y),
                            "Connector must reach both card edges through the year label: dark=$dark scale=$scale y=$y")
                    }
                } }
                current.config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(1, backs)
                assertEquals(listOf(2022), opened)
                if (scale == 1f) scene.save("archive-timeline-${if (dark) "dark" else "light"}")
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { t -> t.text } }
    private fun ImageComposeScene.card(date: String) = nodes().single { it.config.contains(SemanticsActions.OnClick) &&
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == date } == true }
    private fun ImageComposeScene.assertTextFits() {
        // GetTextLayoutResult is only available on the unmerged text nodes.
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.forEach { n ->
            val layouts = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.filter { it.layoutInput.maxLines == Int.MAX_VALUE }.forEach {
                assertTrue(it.multiParagraph.height <= it.size.height + 1, "Clipped ${it.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}

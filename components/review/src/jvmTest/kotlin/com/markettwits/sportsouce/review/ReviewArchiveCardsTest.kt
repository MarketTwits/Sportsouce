package com.markettwits.sportsouce.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.review.review.presentation.components.archive.ArchiveStarts
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ReviewArchiveCardsTest {
    private val start = StartsListItem(315, "Кросс трейл Альбатрос", "", "20 сентября 2026",
        StartsListItem.StatusCode(6, "Завершён"), "", "", "Новосибирск", false, "12 км", views = 0)
    private val starts = listOf(start, start.copy(id = 316, name = "Забег"), start.copy(id = 317,
        name = "Очень длинное название ежегодного спортивного старта", dateRange = "31 декабря 2025 — 1 января 2026"),
        start.copy(id = 318, name = "Юрманка трейл", dateRange = "17–18 октября 2026"),
        start.copy(id = 319, name = "Весенний забег"),
        start.copy(id = 320, name = "Новогодний забег"))

    @Test fun archiveCardsKeepTheirHeightAndEntireCaptionsOpenTheMatchingStart() {
        for (width in listOf(320, 390, 800)) for (scale in listOf(1f, 2f)) for (dark in listOf(false, true)) {
            val opened = mutableListOf<StartsListItem>()
            ImageComposeScene(width, 650, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        ArchiveStarts(starts = starts, onClick = { opened += it })
                    }
                }
            }.use { scene ->
                scene.settle()
                fun viewport() = scene.nodes().single { it.config.contains(SemanticsProperties.HorizontalScrollAxisRange) }
                val height = scene.card(starts.first().name).size.height
                assertEquals(10f, scene.card(starts.first().name).boundsInRoot.left, .5f)
                assertEquals(0f, viewport().boundsInRoot.left, .5f)
                assertEquals(width.toFloat(), viewport().boundsInRoot.right, .5f)
                viewport().config[SemanticsActions.ScrollBy].action!!.invoke(5f, 0f)
                scene.settle()
                assertEquals(5f, scene.card(starts.first().name).boundsInRoot.left, .5f)
                starts.forEachIndexed { index, item ->
                    viewport().config[SemanticsActions.ScrollToIndex].action!!.invoke(index)
                    scene.settle()
                    assertEquals(height, scene.card(item.name).size.height, "Short names and date ranges must not resize the carousel")
                    val date = scene.card(item.name).children.flatMap(::descendants).first { node ->
                        node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == item.displayDate }
                    }
                    scene.sendPointerEvent(PointerEventType.Press, date.boundsInRoot.center)
                    scene.sendPointerEvent(PointerEventType.Release, date.boundsInRoot.center)
                    scene.settle()
                    assertEquals(item, opened.last(), "The caption must open its own start")
                    scene.assertTextFits()
                }
                assertEquals(width - 10f, scene.card(starts.last().name).boundsInRoot.right, .5f)
                if (width == 390 && scale == 1f) {
                    viewport().config[SemanticsActions.ScrollToIndex].action!!.invoke(0)
                    scene.settle()
                    scene.save("review-archive-${if (dark) "dark" else "light"}")
                }
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun descendants(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::descendants)
    private fun ImageComposeScene.nodes() = semanticsOwners.flatMap { descendants(it.unmergedRootSemanticsNode) }
    private fun ImageComposeScene.card(title: String): SemanticsNode {
        var node = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == title } }
        while (!node.config.contains(SemanticsActions.OnClick)) node = node.parent!!
        return node
    }
    private fun ImageComposeScene.assertTextFits() {
        nodes().forEach { node ->
            if (node.boundsInRoot.isEmpty) return@forEach
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { layout ->
                assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Text clipped vertically: ${layout.layoutInput.text}")
                for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) - layout.getLineLeft(line) <= layout.size.width + 1f,
                    "Text clipped horizontally: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}

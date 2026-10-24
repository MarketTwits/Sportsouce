package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.start.components.StartExtraFieldsPanel
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartCardV2
import com.markettwits.sportsouce.starts.common.presentation.StartCardV3
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartDateLayoutTest {
    @Test fun dateRangesRemainReadableInDetailsAndDoNotOverlapCardSlots() {
        for (dark in listOf(false, true)) for (width in listOf(320, 390)) for (scale in listOf(1f, 2f)) {
            for ((index, range) in listOf("17–18 октября 2026", "30 октября — 2 ноября 2026", "31 декабря 2026 — 2 января 2027").withIndex()) {
                val start = StartsListItem(309, "Юрманка трейл", "", "17 октября 2026", StartsListItem.StatusCode(6, "Старт завершён"),
                    "", "yurmanka-trejl-309", "Новосибирская область", false, "8 км, 15 км, 28 км", views = 100,
                    organizers = listOf(StartsListItem.Organizer("Спорт Союз", "", true)),
                    slots = StartsListItem.Slots("25", "200", true), dateRange = range)
                ImageComposeScene(width = width, height = 1800, density = Density(1f, scale)) {
                    SauceTheme(darkTheme = dark) {
                        Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState())) {
                            StartExtraFieldsPanel(Modifier.padding(16.dp), start.place, emptyList(), range)
                            StartCardV2(start = start, onItemClick = {})
                            StartCardV3(start = start, onItemClick = {})
                        }
                    }
                }.use { scene ->
                    repeat(12) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                    fun nodes(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::nodes)
                    val all = scene.semanticsOwners.flatMap { nodes(it.unmergedRootSemanticsNode) }
                    fun texts(text: String) = all.filter { it.config.getOrNull(SemanticsProperties.Text)?.any { it.text == text } == true }
                    val dates = texts(range).sortedBy { it.positionInRoot.y }
                    assertEquals(3, dates.size, "All date surfaces must use the full range")
                    val details = mutableListOf<TextLayoutResult>()
                    dates.first().config[SemanticsActions.GetTextLayoutResult].action!!.invoke(details)
                    assertTrue(details.single().size.height >= details.single().multiParagraph.height - 1)
                    val date = dates.last().parent!!
                    val slots = texts("25/200").single().parent!!
                    assertFalse(date.boundsInRoot.overlaps(slots.boundsInRoot), "Date overlaps slots: $range, width=$width font=$scale")
                    if (width == 390 && scale == 1f && index == 0) {
                        scene.render(System.nanoTime() + 2_000_000_000).use { image -> image.encodeToData()!!.use { data ->
                            File("build/design-system/start-dates-${if (dark) "dark" else "light"}.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                        } }
                    }
                }
            }
        }
    }
}

package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartCardsDesignTest {
    private val start = StartsListItem(315, "Кросс трейл Альбатрос", "", "20 сентября 2026", StartsListItem.StatusCode(3, ""),
        "", "albatros", "Новосибирск, комплекс Альбатрос", false, "Дистанции: 6 км, 12 км, 18 км",
        listOf(StartsListItem.KindOfSport(1, "бег")),
        organizers = listOf(StartsListItem.Organizer("Спорт Союз", "", true)), slots = StartsListItem.Slots("25", "200", true), views = 123)

    @Test
    fun listStatusIsLargerAndReviewStatusMatchesDateWithoutOverlappingSlots() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (width in listOf(320, 390)) for (code in listOf(3, 6)) {
            val event = start.copy(statusCode = StartsListItem.StatusCode(code, ""))
            val opened = mutableListOf<StartsListItem>()
            ImageComposeScene(width = width, height = 1400, density = Density(1f, scale)) {
                AppPalette(dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState())) {
                    StartCardV2(start = event, onItemClick = { opened += it })
                    StartCardV3(start = event, onItemClick = { opened += it })
                } }
            }.use { scene ->
                scene.settle(); scene.assertUnboundedTextFits()
                val textNodes = scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }
                val labels = setOf(startStatusCompactMessage(code), startStatusMessage(code))
                val statuses = textNodes.filter { n -> n.config.getOrNull(SemanticsProperties.Text)?.any { it.text in labels } == true }
                    .sortedBy { it.positionInRoot.y }.map { it.parent!! }
                assertEquals(2, statuses.size)
                assertTrue(statuses[0].boundsInRoot.height >= 32f, "StartsScreen status must be at least 32 dp")
                val dates = textNodes.filter { n -> n.config.getOrNull(SemanticsProperties.Text)?.any { it.text == event.date } == true }.sortedBy { it.positionInRoot.y }
                assertEquals(2, dates.size)
                val reviewDate = dates[1].parent!!
                assertEquals(reviewDate.boundsInRoot.height, statuses[1].boundsInRoot.height, 0.5f,
                    "Review status and date must have the same height: dark=$dark scale=$scale width=$width code=$code")
                assertTrue(reviewDate.boundsInRoot.height >= 32f)
                val slots = textNodes.single { n -> n.config.getOrNull(SemanticsProperties.Text)?.any { it.text == "25/200" } == true }.parent!!
                assertFalse(slots.boundsInRoot.overlaps(reviewDate.boundsInRoot), "Slot count must not cover the date")
                assertFalse(slots.boundsInRoot.overlaps(statuses[1].boundsInRoot), "Slot count must not cover the status")
                val cards = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
                assertEquals(2, cards.size)
                if (scale == 1f && width == 390) {
                    assertTrue(cards[1].size.height <= 330, "Review card should be more compact than the original 349 dp fixture")
                }
                cards.forEach { it.config[SemanticsActions.OnClick].action!!.invoke() }
                assertEquals(listOf(event, event), opened, "Both cards must return the unchanged event")
                if (code == 3) scene.save("start-cards-${if (dark) "dark" else "light"}-${width}-${(scale * 100).toInt()}")
            }
        }
    }

    @Composable
    private fun AppPalette(dark: Boolean, content: @Composable () -> Unit) {
        val p = sauceColors(dark)
        SauceTheme(darkTheme = dark, materialColors = p.material.copy(primary = p.surface, secondary = p.action,
            tertiary = p.textPrimary, primaryContainer = p.surfaceRaised, outline = p.textMuted), content = content)
    }

    @Test fun reviewAvailabilityBadgesFitBelowDateAndPreserveCardNavigation() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (width in listOf(320, 390)) {
            for ((albums, results) in listOf(false to false, true to false, false to true, true to true)) {
                val event = start.copy(hasAlbums = albums, hasResults = results)
                val opened = mutableListOf<StartsListItem>()
                ImageComposeScene(width = width, height = 1000, density = Density(1f, scale)) {
                    AppPalette(dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        StartCardV3(start = event, onItemClick = { opened += it })
                    } }
                }.use { scene ->
                    scene.settle()
                    fun text(value: String) = scene.nodes().filter {
                        it.config.getOrNull(SemanticsProperties.Text)?.any { it.text == value } == true
                    }
                    val date = text(event.displayDate).single().parent!!
                    val title = text(event.name).single()
                    val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                    for ((label, visible) in listOf("Фото" to albums, "Результаты" to results)) {
                        assertEquals(if (visible) 1 else 0, text(label).size)
                        if (visible) {
                            val node = text(label).single()
                            val badge = node.parent!!
                            assertTrue(badge.boundsInRoot.top >= date.boundsInRoot.bottom)
                            assertTrue(badge.boundsInRoot.bottom <= title.boundsInRoot.top)
                            assertTrue(badge.boundsInRoot.left >= card.boundsInRoot.left)
                            assertTrue(badge.boundsInRoot.right <= card.boundsInRoot.right)
                            val layouts = mutableListOf<TextLayoutResult>()
                            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                            val layout = layouts.single()
                            assertFalse(layout.isLineEllipsized(0), "Badge ellipsized: $label width=$width scale=$scale")
                            assertTrue(layout.getLineRight(0) <= layout.size.width + 1f, "Badge text clipped: $label")
                            assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Badge height clipped: $label")
                        }
                    }
                    if (albums && results) {
                        assertFalse(text("Фото").single().parent!!.boundsInRoot.overlaps(text("Результаты").single().parent!!.boundsInRoot))
                        if (width == 390) scene.save("review-start-badges-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                    }
                    card.config[SemanticsActions.OnClick].action!!.invoke()
                    assertEquals(listOf(event), opened)
                }
            }
        }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.assertUnboundedTextFits() {
        nodes().forEach { n ->
            val layouts = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.filter { it.layoutInput.maxLines == Int.MAX_VALUE }.forEach {
                assertFalse(it.hasVisualOverflow, "Clipped text: ${it.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}

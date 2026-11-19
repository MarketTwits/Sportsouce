package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.cloud.model.start.fields.File as StartFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.start.components.StartStatus
import com.markettwits.sportsouce.starts.common.presentation.startStatusMessage
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartStatusLayoutTest {
    @Test fun longStatusEllipsizesWithoutMovingSportsOrOrganizerToAnotherLine() {
        val sports = listOf(StartItem.KindOfSport(1, "Бег"), StartItem.KindOfSport(2, "Лыжи"))
        val organizer = Organizer(1, "", true, StartFile("file:///missing-status-logo.png", 1, "logo.png", ""),
            "Клуб", null, emptyList(), 309)
        for (dark in listOf(false, true)) for (width in listOf(280, 320, 390)) for (scale in listOf(1f, 2f)) {
            var previousSports: List<androidx.compose.ui.geometry.Rect>? = null
            for (code in listOf(5, 3, 4)) {
                ImageComposeScene(width = width, height = 150, density = Density(1f, scale)) {
                    SauceTheme(darkTheme = dark) {
                        Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                            StartStatus(organizers = listOf(organizer), kindOfSports = sports,
                                startStatus = StartItem.StartStatus(code, startStatusMessage(code)))
                        }
                    }
                }.use { scene ->
                    scene.settle()
                    val icons = sports.map { sport -> scene.nodes().single {
                        it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(sport.name) == true
                    }.boundsInRoot }
                    assertEquals(icons[0].center.y, icons[1].center.y, .5f, "Sports stay on the same line")
                    assertEquals(width - 16f - 40f - 8f - 20f, icons[1].center.x, .5f,
                        "The organizer retains its 40 dp slot at the right edge")
                    previousSports?.let { assertEquals(it, icons, "Changing the status must not move or shrink the badges") }
                    previousSports = icons
                    val text = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text)?.any {
                        value -> value.text == startStatusMessage(code)
                    } == true }
                    val layouts = mutableListOf<TextLayoutResult>()
                    text.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    val layout = layouts.single()
                    assertEquals(1, layout.lineCount)
                    assertEquals(1, layout.layoutInput.maxLines)
                    assertEquals(TextOverflow.Ellipsis, layout.layoutInput.overflow)
                    assertTrue(text.boundsInRoot.right < icons[0].left, "Status must not cover the sports")
                    // Skia can report isLineEllipsized=false for visibly ellipsized text.
                    // Check that the full label exceeds its constrained single-line viewport.
                    if (code == 4) assertTrue(layout.multiParagraph.intrinsics.maxIntrinsicWidth > layout.size.width,
                        "The long status must be constrained to the space left by the badges")
                    if (code == 5 && scale == 1f) {
                        val groupCenter = (text.positionInRoot.x - 11f + text.positionInRoot.x + text.size.width) / 2f
                        val statusWidth = width - 32f - 3 * 40f - 3 * 8f
                        assertEquals(16f + statusWidth / 2f, groupCenter, 1f, "The dot and text remain centered together")
                    }
                    if (code == 4 && width == 320) scene.save("start-status-ellipsis-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                }
            }
        }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(10) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}

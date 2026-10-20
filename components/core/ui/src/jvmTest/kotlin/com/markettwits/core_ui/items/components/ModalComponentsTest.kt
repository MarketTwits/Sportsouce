package com.markettwits.core_ui.items.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData
import com.markettwits.core_ui.items.components.timer.domain.model.TimeUnit
import com.markettwits.core_ui.items.components.timer.screens.TimerDialog
import com.markettwits.core_ui.items.components.timer.screens.TimerState
import com.markettwits.core_ui.items.screens.AlertDialogScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class ModalComponentsTest {
    @Test
    fun timerFitsAtDoubleFontSizeAndKeepsTheApplyActionVisible() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (landscape in listOf(false, true)) {
            val width = if (landscape) 640 else 360
            val height = if (landscape) 360 else 640
            val state = TimerState(mutableStateOf(TimeData(secs = TimeUnit(1, 2))))
            val colors = sauceColors(dark)
            ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark, materialColors = colors.material.copy(primary = colors.surface,
                    secondary = colors.action, tertiary = colors.textPrimary)) {
                    TimerDialog(state, true, {}, {})
                }
            }.use { scene ->
                settle(scene)
                val frameName = "timer-${if (dark) "dark" else "light"}-${scale.toInt()}x" + if (landscape) "-landscape" else ""
                scene.saveFrame(frameName)
                val apply = scene.button("Применить")
                assertTrue(!apply.config.contains(SemanticsProperties.Disabled))
                assertTrue(apply.boundsInRoot.bottom <= height.toFloat() && apply.boundsInRoot.top >= 0f)
                val keys = scene.nodes().filter { node -> node.config.contains(SemanticsActions.OnClick) &&
                    node.texts().singleOrNull() in listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "00") }
                assertEquals(11, keys.size)
                assertTrue(keys.all { it.size.width >= 48 && it.size.height >= 48 })
                for (node in scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
                    val results = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(results)
                    for (layout in results) assertTrue(!layout.hasVisualOverflow,
                        "Timer scale=$scale: ${layout.layoutInput.text}, size=${layout.size}, paragraphWidth=${layout.multiParagraph.width}, lineRight=${layout.getLineRight(layout.lineCount - 1)}, overflowHeight=${layout.didOverflowHeight}")
                }
                if (landscape) {
                    val scroll = scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                    assertTrue(scroll.config[SemanticsActions.ScrollBy].action?.invoke(0f, 10000f) == true)
                    settle(scene)
                    assertTrue(scene.button("00").boundsInRoot.height >= 48f, "The last keypad row cannot be reached")
                    scene.saveFrame("$frameName-scrolled")
                }
            }
        }
    }

    @Test
    fun timerKeypadEnablesApplyAndDeleteRestoresTheEmptyState() {
        val state = TimerState()
        var applied = 0
        ImageComposeScene(width = 360, height = 640) {
            SauceTheme { TimerDialog(state, true, {}, { applied++ }) }
        }.use { scene ->
            settle(scene)
            assertTrue(scene.button("Применить").config.contains(SemanticsProperties.Disabled))
            scene.click(scene.button("1"))
            settle(scene)
            assertTrue(!scene.button("Применить").config.contains(SemanticsProperties.Disabled))
            scene.click(scene.button("Применить"))
            assertEquals(1, applied)
            scene.click(scene.nodes().first { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Удалить цифру") == true })
            settle(scene)
            assertTrue(scene.button("Применить").config.contains(SemanticsProperties.Disabled))
        }
    }

    @Test
    fun longConfirmationCanScrollWithoutLosingItsActions() {
        var dismissed = 0
        ImageComposeScene(width = 360, height = 640, density = Density(1f, 2f)) {
            SauceTheme {
                AlertDialogScreen(onDismissRequest = { dismissed++ }, onClickOk = {},
                    title = "Удалить запись?", text = "Эта запись содержит данные участника. ".repeat(30))
            }
        }.use { scene ->
            settle(scene)
            for (label in listOf("Да", "Нет")) {
                val button = scene.button(label)
                assertTrue(button.boundsInRoot.height >= 48f)
                assertTrue(button.boundsInRoot.bottom <= 640f && button.boundsInRoot.top >= 0f)
            }
            val scroll = scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
            assertTrue(scroll.config[SemanticsActions.ScrollBy].action?.invoke(0f, 10000f) == true)
            settle(scene)
            scene.saveFrame("confirmation-large-text")
            scene.click(scene.button("Нет"))
            assertEquals(1, dismissed)
        }
    }

    private fun ImageComposeScene.button(label: String) = nodes().first { node ->
        node.config.contains(SemanticsActions.OnClick) && label in node.texts()
    }

    private fun ImageComposeScene.click(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }

    private fun SemanticsNode.texts() = config.getOrNull(SemanticsProperties.Text)?.map { it.text }.orEmpty()

    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(12) { scene.render(time + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.saveFrame(name: String) {
        val output = File("build/design-system/$name.png").apply { parentFile.mkdirs() }
        render(System.nanoTime() + 2_000_000_000L).use { image ->
            checkNotNull(image.encodeToData()).use { output.writeBytes(it.bytes) }
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

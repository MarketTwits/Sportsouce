package com.markettwits.core_ui.items.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class FocusAndLoadingTest {
    @Test
    fun customButtonContentStillShowsItsLoadingIndicator() {
        ImageComposeScene(width = 320, height = 120) {
            SauceTheme {
                Column {
                    ButtonContentBase(isLoading = true, content = { Text("Сохранение") }, showContent = true)
                }
            }
        }.use { scene ->
            settle(scene)
            assertTrue(scene.nodes().any { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
            assertTrue(scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
                .config.contains(SemanticsProperties.Disabled))
        }
    }

    @Test
    fun keyboardFocusIsVisibleWithoutResizingTheButtonAndEnterStillActivatesIt() {
        for (dark in listOf(false, true)) {
            val colors = sauceColors(dark)
            val interactions = MutableInteractionSource()
            var clicks = 0
            ImageComposeScene(width = 320, height = 100) {
                SauceTheme(darkTheme = dark) {
                    Column {
                        ButtonContentBase(title = "Продолжить", onClick = { clicks++; Unit },
                            containerColor = colors.action, textColor = colors.onAction,
                            interactionSource = interactions)
                    }
                }
            }.use { scene ->
                settle(scene)
                val button = scene.nodes().first { it.config.contains(SemanticsActions.RequestFocus) }
                val bounds = button.boundsInRoot
                val before = scene.frame()
                assertTrue(button.config[SemanticsActions.RequestFocus].action?.invoke() == true)
                settle(scene)
                val focused = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Focused) == true }
                assertEquals(bounds, focused.boundsInRoot)
                val after = scene.frame()
                assertTrue(!before.contentEquals(after), "Keyboard focus is invisible in dark=$dark")
                File("build/design-system/focus-${if (dark) "dark" else "light"}.png")
                    .apply { parentFile.mkdirs(); writeBytes(after) }
                scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyDown))
                scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyUp))
                assertEquals(1, clicks)
            }
        }
    }

    private fun ImageComposeScene.frame(): ByteArray = render(System.nanoTime() + 2_000_000_000L).use { image ->
        checkNotNull(image.encodeToData()).use { it.bytes }
    }

    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(12) { scene.render(time + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

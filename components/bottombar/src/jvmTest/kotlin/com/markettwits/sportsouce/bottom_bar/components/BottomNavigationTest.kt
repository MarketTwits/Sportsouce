package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
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
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
class BottomNavigationTest {
    @Test
    fun selectedLabelUsesActionColorInBothThemesEvenWithTheLegacyMaterialMapping() {
        for (dark in listOf(false, true)) {
            val colors = sauceColors(dark)
            ImageComposeScene(width = 360, height = 160) {
                SauceTheme(darkTheme = dark, materialColors = colors.material.copy(primary = colors.surface, tertiary = colors.textPrimary)) {
                    Column { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Review, true, {}) }
                }
            }.use { scene ->
                settle(scene)
                scene.saveFrame(if (dark) "dark" else "light")
                val labels = scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }
                for (label in labels) {
                    val result = mutableListOf<TextLayoutResult>()
                    label.config[SemanticsActions.GetTextLayoutResult].action?.invoke(result)
                    val layout = result.single()
                    assertEquals(if (layout.layoutInput.text.text == "Обзор") colors.action else colors.textMuted,
                        layout.layoutInput.style.color)
                }
                assertEquals(3, labels.size)
            }
        }
    }

    @Test
    fun hiddenLabelsStillHaveAccessibleNamesAndEachTabSelectsItsOwnDestination() {
        val destinations = mutableListOf<BottomBarConfiguration>()
        navigationScene { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Home, false, destinations::add) }.use { scene ->
            settle(scene)
            scene.saveFrame("icons-only")
            val tabs = scene.nodes().filter { it.config.contains(SemanticsProperties.Selected) }
            assertEquals(3, tabs.size)
            assertTrue(tabs.all { it.boundsInRoot.height >= 48f })
            assertEquals(listOf("Старты", "Обзор", "Профиль"), tabs.map {
                it.config.getOrNull(SemanticsProperties.ContentDescription)?.single()
            })
            tabs.forEach {
                scene.sendPointerEvent(PointerEventType.Press, it.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, it.boundsInRoot.center)
            }
            assertEquals(defaultMenuItems().map { it.bottomBarConfiguration }, destinations)
        }
    }

    @Test
    fun notificationsAreAnnouncedAndLongCountsAreCappedVisually() {
        val items = defaultMenuItems().mapIndexed { index, item ->
            item.copy(hasNews = index == 1, badgeCount = if (index == 0) 125 else 0)
        }
        navigationScene { BottomNavigation(items, BottomBarConfiguration.Home, true, {}) }.use { scene ->
            settle(scene)
            scene.saveFrame("notifications")
            val states = scene.nodes().mapNotNull { it.config.getOrNull(SemanticsProperties.StateDescription) }
            assertTrue("Уведомлений: 125" in states)
            assertTrue("Есть обновления" in states)
            // Badge text is intentionally hidden from semantics; its full count is announced above.
            val textLayouts = scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }
            assertEquals(3, textLayouts.size)
        }
    }

    @Test
    fun labelsAndTouchTargetsFitAtDoubleFontSize() {
        ImageComposeScene(width = 360, height = 220, density = Density(1f, 2f)) {
            SauceTheme { Column { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Home, true, {}) } }
        }.use { scene ->
            settle(scene)
            scene.saveFrame("large-text")
            for (node in scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
                val result = mutableListOf<TextLayoutResult>()
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(result)
                val layout = result.single()
                assertTrue(!layout.hasVisualOverflow, "${layout.layoutInput.text.text}: size=${layout.size}, constraints=${layout.layoutInput.constraints}, lines=${layout.lineCount}, right=${layout.getLineRight(layout.lineCount - 1)}, width=${layout.didOverflowWidth}, height=${layout.didOverflowHeight}")
            }
        }
    }

    private fun navigationScene(content: @Composable () -> Unit) =
        ImageComposeScene(width = 360, height = 160) { SauceTheme { Column { content() } } }

    private fun settle(scene: ImageComposeScene) {
        repeat(12) { scene.render(it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.saveFrame(name: String) {
        val output = File("build/design-system/bottom-$name.png").apply { parentFile.mkdirs() }
        render(1_500_000_000L).use { image ->
            checkNotNull(image.encodeToData()).use { output.writeBytes(it.bytes) }
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

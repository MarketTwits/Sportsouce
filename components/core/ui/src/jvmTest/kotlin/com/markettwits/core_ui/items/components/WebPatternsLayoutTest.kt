package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.input.key.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.catalog.SauceWebPatternsCatalog
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class WebPatternsLayoutTest {
    @Test
    fun activeSegmentHasAVisibleBoundaryAndDistinctFillInBothThemes() {
        for (dark in listOf(false, true)) {
            var selected by mutableIntStateOf(0)
            ImageComposeScene(width = 360, height = 100) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        SauceSegmentedTabs(listOf("Пароль", "SMS (Beta)"), selected, { selected = it })
                    }
                }
            }.use { scene ->
                for (index in 0..1) {
                    selected = index
                    scene.settle()
                    val tabs = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Tab }
                    val active = tabs.single { it.config[SemanticsProperties.Selected] }.boundsInRoot
                    val inactive = tabs.single { !it.config[SemanticsProperties.Selected] }.boundsInRoot
                    scene.render(System.nanoTime() + 2_000_000_000L).use { image ->
                        val pixels = image.toComposeImageBitmap().toPixelMap()
                        val border = pixels[active.center.x.toInt(), active.top.toInt()]
                        val fill = pixels[active.center.x.toInt(), active.top.toInt() + 6]
                        val otherFill = pixels[inactive.center.x.toInt(), inactive.top.toInt() + 6]
                        fun luminance(color: Color): Double {
                            fun linear(channel: Float): Double = if (channel <= .04045f) channel / 12.92 else Math.pow((channel + .055) / 1.055, 2.4)
                            return .2126 * linear(color.red) + .7152 * linear(color.green) + .0722 * linear(color.blue)
                        }
                        val a = luminance(border); val b = luminance(otherFill)
                        assertTrue((maxOf(a, b) + .05) / (minOf(a, b) + .05) >= 3,
                            "The selected outline must remain visible in dark=$dark")
                        assertTrue(kotlin.math.abs(fill.blue - otherFill.blue) > .02f,
                            "Selection must also change the fill in dark=$dark")
                    }
                    scene.save("auth-method-${if (dark) "dark" else "light"}-$index")
                }
            }
        }
    }

    @Test
    fun narrowTabsAndLongSwitchLabelsGrowAtTwoHundredPercent() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 1200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SauceSegmentedTabs(listOf("Информация", "Участники", "Результаты"), 0, {})
                        SauceSwitchRow("Напоминания о приближении даты старта", true, {},
                            supportingText = "Сообщать об изменении времени и места проведения соревнования")
                        SauceTextField("Дополнительная информация об участнике", "", {},
                            supportingText = "Пояснение может занимать несколько строк", singleLine = false, minLines = 2)
                    }
                }
            }.use { scene ->
                scene.settle()
                val tabs = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Tab }
                assertEquals(3, tabs.size)
                assertTrue(tabs.all { it.size.width >= 48 && it.size.height >= 48 })
                assertEquals(1, tabs.count { it.config[SemanticsProperties.Selected] })
                assertTrue(tabs.map { it.size.height }.distinct().size == 1)
                assertTrue(scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }.all { it.size.height >= 48 })
                for (node in scene.nodes()) {
                    // Editors intentionally support scrolling; labels and explanations must fit their layout.
                    if (node.config.contains(SemanticsProperties.EditableText)) continue
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped ${it.layoutInput.text} at scale=$scale") }
                }
                scene.save("web-patterns-stress-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    @OptIn(InternalComposeUiApi::class)
    fun largeTabsScrollTheSelectedItemIntoViewForExternalAndKeyboardChanges() {
        var selected by mutableIntStateOf(0)
        ImageComposeScene(width = 320, height = 140, density = Density(1f, 2f)) {
            SauceTheme {
                SauceSegmentedTabs(listOf("Информация", "Участники", "Результаты"), selected, { selected = it })
            }
        }.use { scene ->
            scene.settle()
            assertTrue(scene.nodes().any { it.config.contains(SemanticsProperties.HorizontalScrollAxisRange) })
            selected = 2
            scene.settle()
            fun active() = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true }
            assertTrue(active().boundsInRoot.width >= 48, "External selection is outside the scroll viewport")
            assertTrue(active().boundsInRoot.left >= 0 && active().boundsInRoot.right <= 320)
            active().config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            scene.sendKeyEvent(KeyEvent(Key.DirectionRight, KeyEventType.KeyDown))
            scene.sendKeyEvent(KeyEvent(Key.DirectionRight, KeyEventType.KeyUp))
            scene.settle()
            assertEquals(0, selected)
            assertTrue(active().boundsInRoot.width >= 48, "Keyboard wraparound is outside the scroll viewport")
        }
    }

    @Test
    fun focusIsVisibleWithoutMovingTheSwitchRowOrTab() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(width = 360, height = 200) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        SauceSegmentedTabs(listOf("Обзор", "Участники"), 0, {})
                        SauceSwitchRow("Напоминания", true, {})
                    }
                }
            }.use { scene ->
                scene.settle()
                val targets = scene.nodes().filter { it.config.contains(SemanticsActions.RequestFocus) }
                assertEquals(3, targets.size)
                for (target in targets) {
                    val bounds = target.boundsInRoot
                    val before = scene.frame()
                    assertTrue(target.config[SemanticsActions.RequestFocus].action!!.invoke())
                    scene.settle()
                    assertEquals(bounds, scene.nodes().single { it.id == target.id }.boundsInRoot)
                    assertFalse(before.contentEquals(scene.frame()), "Invisible focus in dark=$dark")
                }
                scene.save("web-patterns-focus-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun renderTheActualCatalogInBothThemesAndTextScales() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = if (scale == 1f) 360 else 320,
                height = if (scale == 1f) 1400 else 3000, density = Density(1f, scale)) {
                SauceWebPatternsCatalog(darkTheme = dark)
            }.use { scene ->
                scene.settle()
                assertEquals(4, scene.nodes().count { it.config.contains(SemanticsProperties.EditableText) })
                assertEquals(2, scene.nodes().count { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
                val lastLabel = scene.nodes().first { node ->
                    node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Регистрация открыта" }
                }
                assertEquals(lastLabel.size.height.toFloat(), lastLabel.boundsInRoot.height,
                    "The catalog snapshot clips its last example")
                scene.save("web-patterns-catalog-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    private fun ImageComposeScene.frame(): ByteArray = render(System.nanoTime() + 2_000_000_000L).use {
        checkNotNull(it.encodeToData()).use { data -> data.bytes }
    }

    private fun ImageComposeScene.save(name: String) {
        File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(frame()) }
    }

    private fun ImageComposeScene.settle() {
        val time = System.nanoTime()
        repeat(12) { render(time + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

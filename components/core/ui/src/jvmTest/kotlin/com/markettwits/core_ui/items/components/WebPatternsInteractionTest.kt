package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.components.selection.SauceSwitch
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class WebPatternsInteractionTest {
    @Test
    fun externalLabelNamesTheEditorAndSpecificErrorReplacesTheHint() {
        var value by mutableStateOf("")
        ImageComposeScene(width = 360, height = 200) {
            SauceTheme {
                SauceTextField("Email", value, { value = it }, supportingText = "Общее пояснение",
                    errorMessage = "Укажите полный адрес")
            }
        }.use { scene ->
            scene.settle()
            val editor = scene.nodes().single { it.config.contains(SemanticsProperties.EditableText) }
            assertEquals(listOf("Email"), editor.config[SemanticsProperties.ContentDescription])
            assertEquals("Укажите полный адрес", editor.config[SemanticsProperties.Error])
            val texts = scene.nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty() }.map { it.text }
            assertTrue("Укажите полный адрес" in texts)
            assertTrue("Общее пояснение" !in texts)
            assertTrue(editor.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("mail@example.ru")))
            scene.settle()
            assertEquals("mail@example.ru", value)
            assertEquals("mail@example.ru", scene.nodes().single { it.config.contains(SemanticsProperties.EditableText) }
                .config[SemanticsProperties.EditableText].text)
        }
    }

    @Test
    fun readOnlyAndDisabledFieldsDoNotAcceptEdits() {
        var edits = 0
        ImageComposeScene(width = 360, height = 300) {
            SauceTheme {
                Column {
                    SauceTextField("Только чтение", "12345", { edits++ }, readOnly = true)
                    SauceTextField("Недоступно", "12345", { edits++ }, enabled = false)
                }
            }
        }.use { scene ->
            scene.settle()
            val fields = scene.nodes().filter { it.config.contains(SemanticsProperties.EditableText) }
            assertEquals(2, fields.size)
            assertFalse(fields.first().config.contains(SemanticsProperties.Disabled))
            assertTrue(fields.last().config.contains(SemanticsProperties.Disabled))
            fields.forEach {
                assertTrue(it.config.getOrNull(SemanticsActions.SetText)?.action?.invoke(AnnotatedString("change")) != true)
            }
            assertEquals(0, edits)
        }
    }

    @Test
    fun switchRowIsOneNamedTargetAndBothTextAndTrackToggleItOnce() {
        for (dark in listOf(false, true)) {
            var checked by mutableStateOf(false)
            var changes = 0
            ImageComposeScene(width = 360, height = 180) {
                SauceTheme(darkTheme = dark) {
                    SauceSwitchRow("Уведомления", checked, { checked = it; changes++ }, supportingText = "Напоминать о старте")
                }
            }.use { scene ->
                scene.settle()
                var targets = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
                assertEquals(1, targets.size)
                assertEquals(Role.Switch, targets.single().config[SemanticsProperties.Role])
                val text = targets.single().config[SemanticsProperties.Text].map { it.text }
                assertTrue("Уведомления" in text && "Напоминать о старте" in text)
                val bounds = targets.single().boundsInRoot
                assertTrue(bounds.height >= 48)
                scene.click(Offset(bounds.left + 24f, bounds.center.y))
                assertTrue(checked)
                assertEquals(1, changes)
                scene.click(Offset(bounds.right - 32f, bounds.center.y))
                assertFalse(checked)
                assertEquals(2, changes)
                targets = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
                targets.single().config[SemanticsActions.RequestFocus].action!!.invoke()
                scene.settle()
                scene.press(Key.Spacebar)
                scene.settle()
                assertEquals(3, changes)
                assertTrue(checked)
            }
        }
    }

    @Test
    fun disabledSwitchRowAndStandaloneSwitchIgnorePointerEvents() {
        var changes = 0
        ImageComposeScene(width = 360, height = 250) {
            SauceTheme {
                Column {
                    SauceSwitchRow("Недоступно", true, { changes++ }, enabled = false)
                    SauceSwitch(true, { changes++ }, enabled = false)
                }
            }
        }.use { scene ->
            scene.settle()
            val switches = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Switch }
            assertEquals(2, switches.size)
            switches.forEach {
                assertTrue(it.config.contains(SemanticsProperties.Disabled))
                scene.click(it.boundsInRoot.center)
            }
            assertEquals(0, changes)
        }
    }

    @Test
    fun tabsSelectOnceAndIgnoreActivationOfTheCurrentOrDisabledTab() {
        var selected by mutableIntStateOf(0)
        var changes = 0
        ImageComposeScene(width = 360, height = 200) {
            SauceTheme {
                Column {
                    SauceSegmentedTabs(listOf("Обзор", "Участники"), selected, { selected = it; changes++ })
                    SauceSegmentedTabs(listOf("Обзор", "Архив"), 0, { changes++ }, enabled = false)
                }
            }
        }.use { scene ->
            scene.settle()
            fun tabs() = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Tab }
            assertEquals(4, tabs().size)
            scene.click(tabs()[1].boundsInRoot.center)
            assertEquals(1, selected)
            assertEquals(1, changes)
            assertTrue(tabs()[1].config[SemanticsProperties.Selected])
            scene.click(tabs()[1].boundsInRoot.center)
            scene.click(tabs()[3].boundsInRoot.center)
            assertEquals(1, changes)
            tabs()[0].config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            scene.press(Key.Enter)
            scene.settle()
            assertEquals(0, selected)
            assertEquals(2, changes)
        }
    }

    @Test
    fun tabArrowNavigationFollowsLayoutDirectionAndMovesFocusWithSelection() {
        for (direction in listOf(LayoutDirection.Ltr, LayoutDirection.Rtl)) {
            var selected by mutableIntStateOf(1)
            var changes = 0
            ImageComposeScene(width = 360, height = 120) {
                SauceTheme {
                    CompositionLocalProvider(LocalLayoutDirection provides direction) {
                        SauceSegmentedTabs(listOf("Первый", "Второй", "Третий"), selected, { selected = it; changes++ })
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true }
                    .config[SemanticsActions.RequestFocus].action!!.invoke()
                scene.settle()
                scene.press(Key.DirectionRight)
                scene.settle()
                assertEquals(if (direction == LayoutDirection.Ltr) 2 else 0, selected)
                assertEquals(1, changes)
                val active = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true }
                assertTrue(active.config[SemanticsProperties.Focused])
                scene.press(Key.DirectionLeft)
                scene.settle()
                assertEquals(1, selected)
                assertEquals(2, changes)
            }
        }
    }

    @Test
    fun progressHasOneAccessibleValueAndHandlesUnknownAndOutOfRangeData() {
        var progress by mutableStateOf<Float?>(0.65f)
        ImageComposeScene(width = 360, height = 120) {
            SauceTheme { SauceLinearProgressIndicator("Подготовка данных", progress) }
        }.use { scene ->
            scene.settle()
            for ((value, expected) in listOf(0.65f to 0.65f, -1f to 0f, 2f to 1f, 0f to 0f, 1f to 1f)) {
                progress = value
                scene.settle()
                val node = scene.nodes().single { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) }
                assertEquals(expected, node.config[SemanticsProperties.ProgressBarRangeInfo].current)
                assertEquals(listOf("Подготовка данных"), node.config[SemanticsProperties.ContentDescription])
            }
            for (unknown in listOf(null, Float.NaN, Float.POSITIVE_INFINITY)) {
                progress = unknown
                scene.settle()
                assertEquals(ProgressBarRangeInfo.Indeterminate, scene.nodes()
                    .single { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) }
                    .config[SemanticsProperties.ProgressBarRangeInfo])
            }
        }
    }

    @Test
    fun passiveLabelsDoNotAcquireClickOrSelectionSemantics() {
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme { SauceStatusBadge("Бег", showIndicator = false) }
        }.use { scene ->
            scene.settle()
            val nodes = scene.nodes()
            assertTrue(nodes.any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Бег" } == true })
            assertTrue(nodes.none { it.config.contains(SemanticsActions.OnClick) || it.config.contains(SemanticsProperties.Selected) })
        }
    }

    private fun ImageComposeScene.press(key: Key) {
        sendKeyEvent(KeyEvent(key, KeyEventType.KeyDown))
        sendKeyEvent(KeyEvent(key, KeyEventType.KeyUp))
    }

    private fun ImageComposeScene.click(position: Offset) {
        sendPointerEvent(PointerEventType.Press, position)
        sendPointerEvent(PointerEventType.Release, position)
        settle()
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

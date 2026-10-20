package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.selection.*
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class UnifiedControlsTest {
    @Test
    fun checkboxHasOneTargetAndTogglesOnceFromItsLabelMarkerAndKeyboard() {
        var checked by mutableStateOf(false)
        var changes = 0
        ImageComposeScene(width = 360, height = 150) {
            SauceTheme { SauceCheckboxRow("Командный зачёт", checked, { checked = it; changes++ },
                supportingText = "Участвовать вместе с командой") }
        }.use { scene ->
            scene.settle()
            val row = scene.targets().single()
            assertEquals(Role.Checkbox, row.config[SemanticsProperties.Role])
            val texts = row.config[SemanticsProperties.Text].map { it.text }
            assertTrue("Командный зачёт" in texts && "Участвовать вместе с командой" in texts)
            val bounds = row.boundsInRoot
            scene.click(Offset(bounds.right - 24f, bounds.center.y))
            assertTrue(checked)
            assertEquals(1, changes)
            scene.click(Offset(bounds.left + 20f, bounds.center.y))
            assertFalse(checked)
            assertEquals(2, changes)
            scene.targets().single().config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            scene.press(Key.Spacebar)
            scene.settle()
            assertEquals(3, changes)
            assertTrue(checked)
        }
    }

    @Test
    fun radioRowsExposeSingleSelectionAndDisabledRowsIgnoreClicks() {
        var selected by mutableIntStateOf(0)
        var changes = 0
        ImageComposeScene(width = 360, height = 250) {
            SauceTheme {
                Column(Modifier.selectableGroup()) {
                    SauceRadioRow("Взрослый", selected == 0, { selected = 0; changes++ })
                    SauceRadioRow("Ребёнок", selected == 1, { selected = 1; changes++ })
                    SauceCheckboxRow("Недоступно", false, { changes++ }, enabled = false)
                }
            }
        }.use { scene ->
            scene.settle()
            assertEquals(3, scene.targets().size)
            val disabled = scene.targets().last()
            assertTrue(disabled.config.contains(SemanticsProperties.Disabled))
            scene.click(disabled.boundsInRoot.center)
            assertEquals(0, changes)
            scene.click(scene.targets()[1].boundsInRoot.center)
            assertEquals(1, changes)
            assertEquals(1, selected)
            val radios = scene.targets().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.RadioButton }
            assertEquals(1, radios.count { it.config[SemanticsProperties.Selected] })
            assertTrue(radios[1].config[SemanticsProperties.Selected])
        }
    }

    @Test
    fun customCheckboxLabelsPreserveFormattedPricesAndRequiredMarkers() {
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme {
                SauceCheckboxRow(checked = false, onCheckedChange = {}, label = { Text("Участие (+ 500 ₽) *") })
            }
        }.use { scene ->
            scene.settle()
            assertEquals(listOf("Участие (+ 500 ₽) *"),
                scene.targets().single().config[SemanticsProperties.Text].map { it.text })
        }
    }

    @Test
    fun borderlessSearchKeepsEditingAndImeSearchCallbacks() {
        var query by mutableStateOf("")
        var applies = 0
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme {
                BoundlessTextFieldBase(value = query, onValueChange = { query = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { applies++ }))
            }
        }.use { scene ->
            scene.settle()
            val editor = scene.nodes().single { it.config.contains(SemanticsProperties.EditableText) }
            assertTrue(editor.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("Трейл")))
            scene.settle()
            assertEquals("Трейл", query)
            assertTrue(scene.nodes().single { it.config.contains(SemanticsProperties.EditableText) }
                .config[SemanticsActions.OnImeAction].action!!.invoke())
            assertEquals(1, applies)
        }
    }

    @Test
    fun readOnlyCommentPreviewOpensOnceWithoutExposingAnEditableAction() {
        var opens = 0
        var edits = 0
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme {
                BoundlessTextFieldBase(value = "", placeholder = { Text("Ваш комментарий") },
                    readOnly = true, onClick = { opens++ }, onValueChange = { edits++ })
            }
        }.use { scene ->
            scene.settle()
            val field = scene.targets().single()
            assertFalse(field.config.contains(SemanticsProperties.Disabled))
            assertFalse(field.config.contains(SemanticsActions.SetText))
            val unfocused = scene.frame()
            field.config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            assertFalse(unfocused.contentEquals(scene.frame()), "Read-only preview has no visible keyboard focus")
            scene.click(field.boundsInRoot.center)
            assertEquals(1, opens)
            scene.targets().single().config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            scene.press(Key.Enter)
            assertEquals(2, opens)
            assertEquals(0, edits)
        }
    }

    @Test
    fun scrollableTabsKeepMarkersAndKeyboardNavigationInBothDirections() {
        for (direction in listOf(LayoutDirection.Ltr, LayoutDirection.Rtl)) {
            var selected by mutableIntStateOf(1)
            var changes = 0
            ImageComposeScene(width = 360, height = 120) {
                SauceTheme {
                    CompositionLocalProvider(LocalLayoutDirection provides direction) {
                        SauceScrollableTabs(listOf("Обзор", "Участники", "Оплата", "Поделиться"), selected,
                            { selected = it; changes++ }, trailingContent = { index ->
                                if (index == 1) Text("Есть результаты")
                            })
                    }
                }
            }.use { scene ->
                scene.settle()
                fun active() = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true }
                assertTrue(active().config[SemanticsProperties.Text].any { it.text == "Есть результаты" })
                active().config[SemanticsActions.RequestFocus].action!!.invoke()
                scene.settle()
                scene.press(Key.DirectionRight)
                scene.settle()
                assertEquals(if (direction == LayoutDirection.Ltr) 2 else 0, selected)
                assertEquals(1, changes)
                assertTrue(active().config[SemanticsProperties.Focused])
                scene.press(Key.DirectionLeft)
                scene.settle()
                assertEquals(1, selected)
                assertEquals(2, changes)
            }
        }
    }

    @Test
    fun unifiedControlsFitLongLabelsAndLargeTextInBothThemes() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var selected by mutableIntStateOf(1)
            ImageComposeScene(width = 320, height = 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SauceScrollableTabs(listOf("Главная", "Ближайшие", "Прошедшие", "Анонсы"), selected, { selected = it })
                        SauceCheckboxRow("В списке нет моего города", true, {},
                            supportingText = "Укажите название вручную")
                        SauceCheckboxRow("Недоступный вариант", false, {}, enabled = false)
                        SauceRadioRow("Взрослый участник", true, {})
                        SauceRadioRow("Ребёнок", false, {})
                        BoundlessTextFieldBase(value = "", placeholder = { Text("Поиск товара") }, onValueChange = {})
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.targets().all { it.size.height >= 48 && it.size.width >= 48 })
                for (node in scene.nodes()) {
                    if (node.config.contains(SemanticsProperties.EditableText)) continue
                    val result = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(result)
                    result.forEach { assertFalse(it.hasVisualOverflow, "Clipped ${it.layoutInput.text} at $scale: size=${it.size}, width=${it.didOverflowWidth}, height=${it.didOverflowHeight}, paragraph=${it.multiParagraph.width}, intrinsic=${it.multiParagraph.intrinsics.maxIntrinsicWidth}, font=${it.layoutInput.style.fontSize}") }
                }
                selected = 3
                scene.settle()
                val active = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true &&
                    it.config.getOrNull(SemanticsProperties.Role) == Role.Tab }
                assertTrue(active.boundsInRoot.width >= 48, "External selection is outside the scroll viewport")
                scene.save("unified-controls-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun checkboxAndRadioFocusRemainsVisibleWithoutResizingRows() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(width = 320, height = 200) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        SauceCheckboxRow("Командный зачёт", true, {})
                        SauceRadioRow("Системная тема", true, {})
                    }
                }
            }.use { scene ->
                scene.settle()
                for (target in scene.targets()) {
                    val before = scene.frame()
                    val bounds = target.boundsInRoot
                    target.config[SemanticsActions.RequestFocus].action!!.invoke()
                    scene.settle()
                    assertEquals(bounds, scene.nodes().single { it.id == target.id }.boundsInRoot)
                    assertFalse(before.contentEquals(scene.frame()), "Focus is invisible in dark=$dark")
                }
                scene.save("unified-controls-focus-${if (dark) "dark" else "light"}")
            }
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

    private fun ImageComposeScene.targets() = nodes().filter { it.config.contains(SemanticsActions.OnClick) }
}

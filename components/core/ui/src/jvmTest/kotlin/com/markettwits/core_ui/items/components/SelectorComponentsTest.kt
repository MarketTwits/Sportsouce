package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class SelectorComponentsTest {
    @Test
    fun legacyDisabledDecorationsBecomeOneNamedReadOnlySelectorAndLeaveRealDisabledFieldsAlone() {
        for (dark in listOf(false, true)) ImageComposeScene(width = 360, height = 360) {
            SauceTheme(darkTheme = dark) {
                Column(Modifier.background(SauceTheme.colors.background).padding(16.dp)) {
                    DropDownSpinner(selectedItem = "Новосибирск", itemList = listOf("Новосибирск", "Томск"),
                        onItemSelected = { _, _ -> }, textFiled = {
                            OutlinedTextFieldBase(label = "Город", value = "Новосибирск", isEnabled = false, onValueChange = {})
                        })
                    OutlinedTextFieldBase(label = "Недоступное поле", value = "Значение", isEnabled = false, onValueChange = {})
                }
            }
        }.use { scene ->
            settle(scene)
            val buttons = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) && !it.config.contains(SemanticsProperties.Disabled) }
            assertEquals(1, buttons.size, "A selector must have one accessible activation target: ${buttons.map { it.config }}")
            assertTrue(buttons.single().texts().joinToString().let { "Город" in it && "Новосибирск" in it })
            assertTrue(!buttons.single().config.contains(SemanticsActions.SetText))
            assertTrue(scene.nodes().any { it.config.contains(SemanticsProperties.Disabled) })
            assertTrue(buttons.single().config[SemanticsActions.RequestFocus].action?.invoke() == true)
            settle(scene)
            scene.saveFrame("selector-${if (dark) "dark" else "light"}-focused")
            scene.key(Key.Enter)
            settle(scene)
            assertTrue(scene.nodes().any { "Томск" in it.texts() })
            val selected = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Selected) == true }
            assertEquals(listOf("Новосибирск"), selected.texts())
        }
    }

    @Test
    fun pointerSelectionEmitsOnceAndClosesSingleSelect() {
        val selection = mutableStateOf("Бег")
        var selections = 0
        ImageComposeScene(width = 360, height = 640) {
            SauceTheme {
                DropDownSpinner(selectedItem = selection.value, itemList = listOf("Бег", "Плавание"),
                    onItemSelected = { index, value -> assertEquals(1, index); selection.value = value; selections++ },
                    textFiled = { OutlinedTextFieldBase(label = "Спорт", value = selection.value, isEnabled = false, onValueChange = {}) })
            }
        }.use { scene ->
            settle(scene)
            scene.tap(scene.nodes().first { it.texts().any { "Спорт" in it } })
            settle(scene)
            scene.tap(scene.nodes().first { "Плавание" in it.texts() && it.config.contains(SemanticsActions.OnClick) })
            settle(scene)
            assertEquals(1, selections)
            assertEquals("Плавание", selection.value)
            assertTrue(scene.nodes().none { it.config.contains(SemanticsProperties.Selected) })
        }
    }

    @Test
    fun multipleSelectionRetainsTheMenuAndAnnouncesTheUpdatedCheckedState() {
        val selected = mutableStateOf(emptyList<String>())
        var callbacks = 0
        ImageComposeScene(width = 360, height = 640, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) {
                DropDownSpinnerStage(selectedItem = selected.value, itemList = listOf("Бег", "Плавание"),
                    defaultText = "Выберите спорт", onItemSelected = { _, item ->
                        callbacks++
                        selected.value = if (item in selected.value) selected.value - item else selected.value + item
                    }, textFiled = { OutlinedTextFieldBase(label = "Спорт", value = selected.value.joinToString(), isEnabled = false, onValueChange = {}) })
            }
        }.use { scene ->
            settle(scene)
            val anchor = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
            assertTrue("Спорт" in anchor.texts(), "The placeholder must not replace the field label")
            assertTrue((anchor.texts() + listOfNotNull(anchor.config.getOrNull(SemanticsProperties.StateDescription))).any { "Выберите спорт" in it })
            scene.tap(anchor)
            settle(scene)
            fun option() = scene.nodes().first { "Бег" in it.texts() && it.config.contains(SemanticsProperties.ToggleableState) }
            scene.tap(option())
            settle(scene)
            assertEquals(1, callbacks)
            assertEquals(ToggleableState.On, option().config[SemanticsProperties.ToggleableState])
            assertTrue(scene.nodes().none { it.texts().any { "Выберите спорт" in it } ||
                it.config.getOrNull(SemanticsProperties.StateDescription)?.contains("Выберите спорт") == true })
            scene.saveFrame("selector-multiple-dark-2x")
            scene.tap(option())
            settle(scene)
            assertEquals(2, callbacks)
            assertEquals(ToggleableState.Off, option().config[SemanticsProperties.ToggleableState])
        }
    }

    @Test
    fun disabledSelectorCannotOpenAndDoesNotReopenWhenEnabledAgain() {
        val enabled = mutableStateOf(true)
        ImageComposeScene(width = 360, height = 640) {
            SauceTheme {
                DropDownSpinner(selectedItem = "Бег", itemList = listOf("Бег", "Плавание"), enabled = enabled.value,
                    onItemSelected = { _, _ -> }, textFiled = {
                        OutlinedTextFieldBase(label = "Спорт", value = "Бег", onValueChange = {})
                    })
            }
        }.use { scene ->
            settle(scene)
            scene.tap(scene.nodes().first { it.config.contains(SemanticsActions.OnClick) })
            settle(scene)
            assertTrue(scene.nodes().any { "Плавание" in it.texts() })
            enabled.value = false
            settle(scene)
            val anchor = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
            assertTrue(anchor.config.contains(SemanticsProperties.Disabled))
            scene.tap(anchor)
            settle(scene)
            assertTrue(scene.nodes().none { "Плавание" in it.texts() })
            enabled.value = true
            settle(scene)
            assertTrue(scene.nodes().none { "Плавание" in it.texts() })
        }
    }

    @Test
    fun dateAndTimeDecorationsCanOpenFromTheKeyboardWithoutBecomingEditable() {
        for (calendar in listOf(false, true)) ImageComposeScene(width = 400, height = 760) {
            SauceTheme {
                if (calendar) CalendarTextFiled(textFiled = { modifier ->
                    OutlinedTextFieldBase(modifier, "Дата", "", isEnabled = false, onValueChange = {})
                }, onValueChanged = {})
                else TimeTextField(textFiled = { modifier ->
                    OutlinedTextFieldBase(modifier, "Время", "", isEnabled = false, onValueChange = {})
                }, onValueChanged = {})
            }
        }.use { scene ->
            settle(scene)
            val anchor = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
            assertTrue(!anchor.config.contains(SemanticsProperties.Disabled))
            assertTrue(!anchor.config.contains(SemanticsActions.SetText))
            assertTrue(anchor.config[SemanticsActions.RequestFocus].action?.invoke() == true)
            settle(scene)
            scene.key(Key.Spacebar)
            settle(scene)
            assertTrue(scene.nodes().any { if (calendar) "Отменить" in it.texts() else "Применить" in it.texts() })
        }
    }

    private fun SemanticsNode.texts() = config.getOrNull(SemanticsProperties.Text)?.map { it.text }.orEmpty()
    private fun ImageComposeScene.tap(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }
    private fun ImageComposeScene.key(key: Key) {
        sendKeyEvent(KeyEvent(key, KeyEventType.KeyDown))
        sendKeyEvent(KeyEvent(key, KeyEventType.KeyUp))
    }
    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(12) { scene.render(time + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.saveFrame(name: String) {
        File("build/design-system/$name.png").apply {
            parentFile.mkdirs()
            render(System.nanoTime() + 2_000_000_000L).use { image ->
                checkNotNull(image.encodeToData()).use { writeBytes(it.bytes) }
            }
        }
    }
}

package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.components.textField.DropDownSpinnerStage
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class FieldLayoutTest {
    @Test
    fun missingTrailingIconDoesNotReserveAnEmptyIconSlot() {
        ImageComposeScene(width = 320, height = 240) {
            SauceTheme {
                Column {
                    BoundlessTextFieldBase(value = "Поиск по названию", onValueChange = {})
                    BoundlessTextFieldBase(value = "Поиск по названию", onValueChange = {},
                        trailingIcon = { Icon(Icons.Default.Search, null, Modifier.size(24.dp)) })
                }
            }
        }.use { scene ->
            settle(scene)
            val fields = scene.nodes().filter { it.config.contains(SemanticsProperties.EditableText) }
            val widths = fields.map { node ->
                val result = mutableListOf<TextLayoutResult>()
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(result)
                result.single().size.width
            }
            assertEquals(2, widths.size)
            assertEquals(288, widths[0], "An icon-free 320dp field keeps only its two 16dp text insets")
            assertTrue(widths[0] >= widths[1] + 32, "Missing icon still consumes space: $widths")
        }
    }

    @Test
    fun selectorOpensOncePerKeyboardActivationWithoutEditingItsValue() {
        var opens = 0
        var edits = 0
        ImageComposeScene(width = 320, height = 120) {
            SauceTheme {
                OutlinedTextFieldBase(label = "Город", value = "Новосибирск", readOnly = true,
                    onClick = { opens++ }, onValueChange = { edits++ })
            }
        }.use { scene ->
            settle(scene)
            val field = scene.nodes().first { it.config.contains(SemanticsActions.RequestFocus) }
            assertTrue(field.config[SemanticsActions.RequestFocus].action?.invoke() == true)
            settle(scene)
            for (key in listOf(Key.Enter, Key.Spacebar)) {
                scene.sendKeyEvent(KeyEvent(key, KeyEventType.KeyDown))
                scene.sendKeyEvent(KeyEvent(key, KeyEventType.KeyUp))
            }
            assertEquals(2, opens)
            assertEquals(0, edits)
        }
    }

    @Test
    fun multiSelectDoesNotPaintItsPlaceholderOverSelectedValues() {
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme {
                DropDownSpinnerStage(selectedItem = listOf("Бег"), defaultText = "Выберите спорт",
                    itemList = listOf("Бег", "Плавание"), onItemSelected = { _, _ -> },
                    textFiled = { Text("Бег") })
            }
        }.use { scene ->
            settle(scene)
            val text = scene.nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty() }.map { it.text }
            assertTrue("Бег" in text)
            assertTrue("Выберите спорт" !in text)
        }
    }

    private fun settle(scene: ImageComposeScene) {
        repeat(12) { scene.render(it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

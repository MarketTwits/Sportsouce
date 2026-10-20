package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class FieldAccessibilityTest {
    @Test
    fun selectorKeepsItsSupportingErrorTextAccessibleWithoutExposingAnEditor() {
        val explanation = "Укажите дистанцию, чтобы продолжить регистрацию"
        ImageComposeScene(width = 360, height = 240) {
            SauceTheme {
                DropDownSpinner(selectedItem = "", itemList = listOf("4 км", "12 км"),
                    onItemSelected = { _, _ -> }, textFiled = {
                        OutlinedTextFieldBase(label = "Дистанция", value = "", isError = true,
                            supportingText = { Text(explanation) }, onValueChange = {})
                    })
            }
        }.use { scene ->
            val time = System.nanoTime()
            repeat(12) { scene.render(time + it * 100_000_000L).close() }
            fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
            val nodes = scene.semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
            val buttons = nodes.filter { it.config.contains(SemanticsActions.OnClick) }
            assertEquals(1, buttons.size)
            val button = buttons.single()
            val texts = button.config.getOrNull(SemanticsProperties.Text).orEmpty().map { it.text }
            assertTrue(explanation in texts, "A screen reader loses the actionable validation message: $texts")
            assertTrue(button.config.contains(SemanticsProperties.Error))
            assertTrue(!button.config.contains(SemanticsActions.SetText))
            assertTrue(!button.config.contains(SemanticsProperties.EditableText))
        }
    }

    @Test
    fun selectorDecorationPreservesNativeFieldDimensionsIncludingSupportingText() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var editableSize = IntSize.Zero
            var selectorSize = IntSize.Zero
            ImageComposeScene(width = 360, height = 480, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column {
                        OutlinedTextFieldBase(modifier = Modifier.onSizeChanged { editableSize = it },
                            label = "Город", value = "Новосибирск", isError = true,
                            supportingText = { Text("Проверьте значение") }, onValueChange = {})
                        DropDownSpinner(selectedItem = "Новосибирск", itemList = listOf("Новосибирск"),
                            onItemSelected = { _, _ -> }, textFiled = {
                                OutlinedTextFieldBase(modifier = Modifier.onSizeChanged { selectorSize = it },
                                    label = "Город", value = "Новосибирск", isError = true, isEnabled = false,
                                    supportingText = { Text("Проверьте значение") }, onValueChange = {})
                            })
                    }
                }
            }.use { scene ->
                val time = System.nanoTime()
                repeat(12) { scene.render(time + it * 100_000_000L).close() }
                assertTrue(editableSize.height >= 56)
                assertEquals(editableSize, selectorSize, "Field layout changed at fontScale=$scale, dark=$dark")
            }
        }
    }

}

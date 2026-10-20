package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class PhoneFieldInteractionTest {
    @Test
    fun formattedPasteAndDeletionInTheMiddleKeepThePhoneContract() {
        var value by mutableStateOf("")
        ImageComposeScene(width = 360, height = 100) {
            SauceTheme {
                OutlinePhoneTextFiled(label = "Телефон", value = value, onValueChange = { value = it })
            }
        }.use { scene ->
            settle(scene)
            scene.field().config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("+7 (913) 456-78-90"))
            settle(scene)
            assertEquals("+7 (913) 456-78-90", value)
            assertEquals("+7 (913) 456-78-90", scene.field().config[SemanticsProperties.EditableText].text)
            val results = mutableListOf<TextLayoutResult>()
            scene.field().config[SemanticsActions.GetTextLayoutResult].action!!.invoke(results)
            assertEquals("+7 (913) 456-78-90", results.single().layoutInput.text.text)
            scene.field().config[SemanticsActions.RequestFocus].action!!.invoke()
            settle(scene)
            scene.field().config[SemanticsActions.SetSelection].action!!.invoke(6, 6, true)
            settle(scene)
            scene.sendKeyEvent(KeyEvent(Key.Backspace, KeyEventType.KeyDown))
            scene.sendKeyEvent(KeyEvent(Key.Backspace, KeyEventType.KeyUp))
            settle(scene)
            assertEquals("+7 (913) 457-89-0", value)
            assertEquals("+7 (913) 457-89-0", scene.field().config[SemanticsProperties.EditableText].text)
        }
    }

    @Test
    fun disabledPhoneIgnoresEditsAndErrorSupportRemainsAccessibleInBothThemes() {
        for (dark in listOf(false, true)) {
            var edits = 0
            ImageComposeScene(width = 360, height = 280) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinePhoneTextFiled(label = "Телефон", value = "913", isError = true,
                            supportingText = { Text("Введите номер полностью") }, onValueChange = {})
                        OutlinePhoneTextFiled(label = "Телефон", value = "9134567890", isEnabled = false,
                            onValueChange = { edits++ })
                    }
                }
            }.use { scene ->
                settle(scene)
                val fields = scene.nodes().filter { it.config.contains(SemanticsProperties.EditableText) }
                assertEquals(2, fields.size)
                assertTrue(fields.first().config.contains(SemanticsProperties.Error))
                assertTrue(scene.nodes().any { node -> node.config.getOrNull(SemanticsProperties.Text).orEmpty()
                    .any { it.text == "Введите номер полностью" } })
                val disabled = fields.last()
                assertTrue(disabled.config.contains(SemanticsProperties.Disabled))
                assertTrue(disabled.config.getOrNull(SemanticsActions.SetText)?.action?.invoke(AnnotatedString("123")) != true)
                assertEquals(0, edits)
                File("build/design-system/phone-fields-${if (dark) "dark" else "light"}.png")
                    .apply { parentFile.mkdirs(); writeBytes(scene.render(System.nanoTime() + 2_000_000_000L).use {
                        checkNotNull(it.encodeToData()).use { data -> data.bytes }
                    }) }
            }
        }
    }

    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(12) { scene.render(time + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.field() = nodes().first { it.config.contains(SemanticsProperties.EditableText) }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

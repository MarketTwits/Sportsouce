package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.use
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.FontRoboto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class ComponentInteractionTest {
    @Test
    fun regularAndMediumLoadDistinctRealFontOutlines() {
        val regular = renderText(FontWeight.Normal) { FontRoboto.regular() }
        val medium = renderText(FontWeight.Medium) { FontRoboto.medium() }
        assertTrue(!regular.contentEquals(medium), "400 and 500 must load distinct real outlines")
    }

    private fun renderText(weight: FontWeight, family: @Composable () -> FontFamily): ByteArray {
        ImageComposeScene(width = 360, height = 96) {
            SauceTheme {
                Surface(color = Color.White, contentColor = Color.Black) {
                    Text("Регистрация открыта", fontFamily = family(), fontWeight = weight,
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }.use { scene ->
            scene.render(0).close()
            scene.render(1_000_000_000).close()
            scene.render(2_000_000_000).use { image ->
                checkNotNull(image.encodeToData()).use { return it.bytes }
            }
        }
    }

    @Test
    fun disabledAndLoadingButtonsIgnoreClicksButTheEnabledButtonWorks() {
        var clicks = 0
        ImageComposeScene(width = 400, height = 400) {
            SauceTheme {
                Column {
                    SauceButton("Обычная", onClick = { clicks++ })
                    SauceButton("Недоступна", onClick = { clicks++ }, enabled = false)
                    SauceButton("Загрузка", onClick = { clicks++ }, loading = true)
                    ButtonContentBase(title = "Сохранение", onClick = { clicks++; Unit }, isEnabled = false, isLoading = true)
                }
            }
        }.use { scene ->
            scene.render(0).close()
            val buttons = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
            assertEquals(4, buttons.size)
            assertTrue(buttons.all { it.boundsInRoot.height >= 48f })
            assertTrue(buttons[1].config.contains(SemanticsProperties.Disabled))
            assertTrue(buttons[2].config.contains(SemanticsProperties.Disabled))
            assertTrue(buttons[3].config.contains(SemanticsProperties.Disabled))
            assertTrue(buttons[3].config.contains(SemanticsProperties.StateDescription))
            buttons.forEach { node ->
                scene.sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
                scene.render(100_000_000).close()
            }
            assertEquals(1, clicks)
        }
    }

    @Test
    fun readOnlySelectorIsClickableWithoutEditingOrDisablingItsValue() {
        var opened = 0
        var edits = 0
        ImageComposeScene(width = 400, height = 180) {
            SauceTheme {
                OutlinedTextFieldBase(
                    label = "Дистанция",
                    value = "4 км",
                    readOnly = true,
                    onClick = { opened++ },
                    onValueChange = { edits++ },
                )
            }
        }.use { scene ->
            scene.render(0).close()
            val field = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
            assertTrue(!field.config.contains(SemanticsProperties.Disabled))
            scene.sendPointerEvent(PointerEventType.Press, field.boundsInRoot.center)
            scene.sendPointerEvent(PointerEventType.Release, field.boundsInRoot.center)
            scene.render(100_000_000).close()
            assertEquals(1, opened)
            assertEquals(0, edits)
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

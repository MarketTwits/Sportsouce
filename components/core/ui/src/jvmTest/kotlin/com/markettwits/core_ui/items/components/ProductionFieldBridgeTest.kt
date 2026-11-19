package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ProductionFieldBridgeTest {
    @Test
    fun legacyFormFocusRequestersStillTargetTheNativeEditors() {
        val first = FocusRequester()
        val second = FocusRequester()
        ImageComposeScene(width = 360, height = 300) {
            SauceTheme {
                Column {
                    OutlinedTextFieldBase(Modifier.focusRequester(first), "Имя", "", onValueChange = {})
                    OutlinedTextFieldBase(Modifier.focusRequester(second), "Фамилия", "", onValueChange = {})
                }
            }
        }.use { scene ->
            scene.settle()
            first.requestFocus()
            scene.settle()
            assertEquals(listOf("Имя"), scene.nodes().single {
                it.config.getOrNull(SemanticsProperties.Focused) == true
            }.config[SemanticsProperties.ContentDescription])
            second.requestFocus()
            scene.settle()
            assertEquals(listOf("Фамилия"), scene.nodes().single {
                it.config.getOrNull(SemanticsProperties.Focused) == true
            }.config[SemanticsProperties.ContentDescription])
        }
    }

    @Test
    fun contactSwitchFitsBesideTheParticipantTitleWithoutClippingAtLargeText() {
        for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 400, density = Density(1f, scale)) {
                SauceTheme {
                    if (scale > 1.3f) {
                        Column(Modifier.fillMaxWidth().background(SauceTheme.colors.surface).padding(12.dp)) {
                            Text("Участник 1", Modifier.fillMaxWidth())
                            SauceSwitchRow("Контактное лицо", true, {})
                        }
                    } else Row(Modifier.fillMaxWidth().background(SauceTheme.colors.surface).padding(12.dp)) {
                        Text("Участник 1", Modifier.weight(1f))
                        SauceSwitchRow("Контактное лицо", true, {},
                            modifier = Modifier.padding(start = 12.dp).width(IntrinsicSize.Min))
                    }
                }
            }.use { scene ->
                scene.settle()
                val title = scene.nodes().single { node ->
                    node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Участник 1" }
                }
                assertTrue(title.size.width >= 48, "The switch consumed the participant title")
                for (node in scene.nodes()) {
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped ${it.layoutInput.text} at scale=$scale, size=${it.size}, lines=${it.lineCount}") }
                }
                File("build/design-system/contact-switch-${(scale * 100).toInt()}.png").apply {
                    parentFile.mkdirs()
                    writeBytes(scene.render(System.nanoTime() + 2_000_000_000L).use {
                        checkNotNull(it.encodeToData()).use { data -> data.bytes }
                    })
                }
            }
        }
    }

    @Test
    fun ratingProgressKeepsItsFivePointUnitsAccessible() {
        ImageComposeScene(width = 320, height = 100) {
            SauceTheme { SauceLinearProgressIndicator("Трасса", 0.8f, valueLabel = "4/5") }
        }.use { scene ->
            scene.settle()
            val progress = scene.nodes().single { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) }
            assertEquals("4/5", progress.config[SemanticsProperties.StateDescription])
            assertEquals(0.8f, progress.config[SemanticsProperties.ProgressBarRangeInfo].current)
        }
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

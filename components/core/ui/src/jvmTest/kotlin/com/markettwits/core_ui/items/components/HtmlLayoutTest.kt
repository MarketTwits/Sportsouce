package com.markettwits.core_ui.items.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.use
import com.markettwits.core_ui.items.text.ExpandableHtmlText
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.DarkSauceColors
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class HtmlLayoutTest {
    private val document = "<p><b>Описание старта</b> " + "Длинное описание с <a href='https://sportsauce.ru/'>ссылкой</a>. ".repeat(16) + "</p>"

    @Test
    fun richTextRespectsLineLimitAndChangesThemeWithoutReparsingContent() {
        val dark = mutableStateOf(false)
        var result: TextLayoutResult? = null
        ImageComposeScene(width = 260, height = 1200) {
            SauceTheme(darkTheme = dark.value) {
                HtmlText(text = document, maxLines = 2, overflow = TextOverflow.Ellipsis, onTextLayout = { result = it })
            }
        }.use { scene ->
            settle(scene, 0L)
            assertEquals(2, assertNotNull(result).lineCount)
            assertTrue(assertNotNull(result).hasVisualOverflow)
            dark.value = true
            settle(scene, 2_000_000_000L)
            assertEquals(DarkSauceColors.textPrimary, assertNotNull(result).layoutInput.style.color)
            assertTrue(assertNotNull(result).layoutInput.text.text.contains("Описание старта"))
        }
    }

    @Test
    fun expandingAndCollapsingHtmlKeepsAValidDocument() {
        ImageComposeScene(width = 320, height = 1200) {
            SauceTheme { ExpandableHtmlText(text = document, collapsedLines = 3) }
        }.use { scene ->
            settle(scene, 0L)
            fun click(label: String, time: Long) {
                val button = scene.nodes().first { node ->
                    node.config.contains(SemanticsActions.OnClick) &&
                        node.config.getOrNull(SemanticsProperties.Text)?.any { it.text == label } == true
                }
                scene.sendPointerEvent(PointerEventType.Press, button.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, button.boundsInRoot.center)
                settle(scene, time)
            }
            click("Подробнее", 2_000_000_000L)
            click("Скрыть", 4_000_000_000L)
            assertTrue(scene.nodes().any { node -> node.config.getOrNull(SemanticsProperties.Text)?.any { it.text == "Подробнее" } == true })
        }
    }

    @Test
    fun clippedListsSurviveFontScaleChangesAndKeepNativeLinks() {
        val scale = mutableStateOf(1f)
        var result: TextLayoutResult? = null
        val html = "<ol>" + (1..6).joinToString("") {
            "<li>Дистанция $it: <a href='https://sportsauce.ru/'>подробнее <b>о старте</b></a></li>"
        } + "</ol>"
        ImageComposeScene(width = 260, height = 120) {
            CompositionLocalProvider(LocalDensity provides Density(1f, scale.value)) {
                SauceTheme {
                    HtmlText(modifier = Modifier.height(35.dp), text = html, onTextLayout = { result = it })
                }
            }
        }.use { scene ->
            settle(scene, 0L)
            scale.value = 2f
            settle(scene, 2_000_000_000L)
            scale.value = 1f
            settle(scene, 4_000_000_000L)
            val text = assertNotNull(result).layoutInput.text
            assertTrue("1. " in text.text && "6. " in text.text)
            assertTrue(text.spanStyles.any { it.item.fontWeight == androidx.compose.ui.text.font.FontWeight.Bold })
            val links = text.getLinkAnnotations(0, text.length)
            assertTrue(links.isNotEmpty())
            assertTrue(links.all { (it.item as? LinkAnnotation.Url)?.url == "https://sportsauce.ru/" })
        }
    }

    private fun settle(scene: ImageComposeScene, time: Long) {
        repeat(12) { frame -> scene.render(time + frame * 100_000_000L).close() }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}

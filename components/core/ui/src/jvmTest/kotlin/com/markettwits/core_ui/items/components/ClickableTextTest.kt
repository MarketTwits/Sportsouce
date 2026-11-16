package com.markettwits.core_ui.items.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.use
import com.markettwits.core_ui.items.text.ClickableText
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@OptIn(ExperimentalComposeUiApi::class)
class ClickableTextTest {
    @Test fun linksKeepQueryNumbersAndUseUpdatedTextAndCallback() {
        val text = mutableStateOf("1 234\n<5 & 10> https://sportsauce.ru/start/318?x=123456789&y=42.")
        val clicks = mutableListOf<String>()
        ImageComposeScene(width = 360, height = 300) {
            SauceTheme { ClickableText(text = text.value, onLinkClick = clicks::add) }
        }.use { scene ->
            scene.settle(0)
            fun clickLink() {
                val body = scene.document()
                assertEquals(text.value, body.text)
                val annotation = body.getLinkAnnotations(0, body.length).single()
                val link = assertIs<LinkAnnotation.Url>(annotation.item)
                assertNotNull(link.linkInteractionListener).onClick(link)
            }
            clickLink()
            assertEquals(listOf("https://sportsauce.ru/start/318?x=123456789&y=42"), clicks)

            text.value = "Новый ответ https://sportsauce.ru/starts/photo/27"
            Snapshot.sendApplyNotifications()
            scene.settle(2_000_000_000)
            clickLink()
            assertEquals("https://sportsauce.ru/starts/photo/27", clicks.last())
        }
    }

    @Test fun phoneLinksRequireAnActionAndDoNotSpanNewlinesOrUrlDigits() {
        val text = "12 34\n56 78; телефон +7 (968) 22-111-22; https://sportsauce.ru/123456789"
        val calls = mutableListOf<String>()
        ImageComposeScene(width = 360, height = 300) {
            SauceTheme { ClickableText(text = text, onPhoneClick = calls::add) }
        }.use { scene ->
            scene.settle(0)
            val body = scene.document()
            assertEquals(text, body.text)
            val links = body.getLinkAnnotations(0, body.length)
            assertEquals(2, links.size)
            val phone = assertIs<LinkAnnotation.Clickable>(links.single { it.item is LinkAnnotation.Clickable }.item)
            assertNotNull(phone.linkInteractionListener).onClick(phone)
            assertEquals(listOf("+7 (968) 22-111-22"), calls)
        }
    }

    private fun ImageComposeScene.settle(time: Long) {
        repeat(6) { render(time + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.document(): AnnotatedString {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        val layouts = mutableListOf<TextLayoutResult>()
        semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.forEach { node ->
            if (node.config.contains(SemanticsActions.GetTextLayoutResult)) {
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
            }
        }
        return layouts.single().layoutInput.text
    }
}

package com.markettwits.sportsouce.starts.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.use
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.starts.presentation.components.StartsChatCard
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartsChatCardTest {
    @Test fun invitationFitsBothThemesAndLargeTextAndOpensFromTheWholeCard() {
        for (width in listOf(320, 390, 800)) for (scale in listOf(1f, 2f)) for (dark in listOf(false, true)) {
            var clicks = 0
            ImageComposeScene(width, 900, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        StartsChatCard(onClick = { clicks++ })
                    }
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun settle() { repeat(8) { time += 100_000_000L; scene.render(time).close() } }
                fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
                fun nodes() = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                settle()
                val card = nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertEquals(10f, card.boundsInRoot.left, .5f)
                assertEquals(width - 10f, card.boundsInRoot.right, .5f)
                assertTrue(card.size.height >= 48)
                val texts = nodes().filter { it.config.contains(SemanticsProperties.Text) }
                assertEquals(2, texts.size)
                texts.forEach { node ->
                    assertEquals(node.size.height.toFloat(), node.boundsInRoot.height, .5f)
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    layouts.forEach { layout ->
                        assertTrue(layout.multiParagraph.height <= layout.size.height + 1f,
                            "Clipped invitation vertically at $width / $scale: ${layout.layoutInput.text}")
                        for (line in 0 until layout.lineCount) {
                            assertTrue(layout.getLineRight(line) - layout.getLineLeft(line) <= layout.size.width + 1f,
                                "Clipped invitation horizontally at $width / $scale: ${layout.layoutInput.text}")
                        }
                    }
                }
                if (width == 390) {
                    time += 500_000_000L
                    scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                        File("build/design-system/chat-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}.png")
                            .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                    } }
                }
                texts.forEach { node ->
                    scene.sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
                    scene.sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
                    settle()
                }
                assertEquals(2, clicks, "Title and description must both open the chat")
            }
        }
    }
}

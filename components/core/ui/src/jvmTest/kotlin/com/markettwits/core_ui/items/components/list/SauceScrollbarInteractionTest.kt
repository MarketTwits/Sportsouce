package com.markettwits.core_ui.items.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class SauceScrollbarInteractionTest {
    @Test fun aShortScrollableListHasAThumbWithAComfortableTouchTarget() = java.awt.EventQueue.invokeAndWait {
        var content by mutableFloatStateOf(900f)
        var offset by mutableFloatStateOf(0f)
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme {
                SauceScrollbar({ SauceScrollbarMetrics(900f, content, offset) }, { false },
                    { offset = it }, Modifier.fillMaxSize(), 90.dp, 30.dp)
            }
        }.use { scene ->
            var time = System.nanoTime()
            fun frames() { repeat(4) { time += 16_000_000L; scene.render(time).close() } }
            fun nodes(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::nodes)
            fun thumb() = scene.semanticsOwners.flatMap { nodes(it.unmergedRootSemanticsNode) }
                .firstOrNull { "Прокрутка списка" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
            frames()
            assertNull(thumb(), "A list that fits needs no scrollbar")
            content = 930f; frames()
            val short = assertNotNull(thumb())
            assertEquals(48f, short.boundsInRoot.height)
            short.config[SemanticsActions.SetProgress].action!!.invoke(.5f)
            frames()
            assertEquals(15f, offset)
        }
    }

    @Test fun onlyTheThumbCapturesTouchesAndDraggingTracksTheFinger() = java.awt.EventQueue.invokeAndWait {
        var offset by mutableFloatStateOf(0f)
        var clicks = 0
        var started = 0
        var stopped = 0
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme {
                Box(Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize().clickable { clicks++ })
                    SauceScrollbar({ SauceScrollbarMetrics(900f, 9000f, offset) }, { false },
                        { offset = it.coerceIn(0f, 8100f) }, Modifier.fillMaxSize(), 90.dp, 30.dp,
                        onDragStarted = { started++ }, onDragStopped = { stopped++ })
                }
            }
        }.use { scene ->
            var time = System.nanoTime()
            fun frames() { repeat(4) { time += 16_000_000L; scene.render(time).close() } }
            fun pointer(type: PointerEventType, point: Offset) {
                time += 16_000_000L
                scene.sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = time / 1_000_000)
                frames()
            }
            frames()
            pointer(PointerEventType.Press, Offset(380f, 650f))
            pointer(PointerEventType.Release, Offset(380f, 650f))
            assertEquals(1, clicks, "The invisible track must pass taps through")
            assertEquals(0f, offset)
            pointer(PointerEventType.Press, Offset(380f, 120f))
            assertEquals(0f, offset, "Grabbing the thumb must not jump the list")
            pointer(PointerEventType.Move, Offset(380f, 220f))
            val firstOffset = offset
            assertTrue(firstOffset > 1000f)
            pointer(PointerEventType.Move, Offset(300f, 320f))
            assertEquals(firstOffset * 2f, offset, 20f, "Moving target coordinates must not amplify or reverse the drag")
            pointer(PointerEventType.Release, Offset(300f, 320f))
            assertEquals(1, clicks)
            assertEquals(1, started); assertEquals(1, stopped)
        }
    }
}

package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.coroutines.runBlocking
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class SauceClickTargetTest {
    @Test fun roundedRippleLeavesEdgeContentIntactAndTheWholeBlockIsOneAction() = runBlocking<Unit> {
        val interactions = MutableInteractionSource()
        var clicks = 0
        ImageComposeScene(width = 160, height = 100) {
            SauceTheme {
                CompositionLocalProvider(LocalContentColor provides Color.Black) {
                    SauceClickTarget(onClick = { clicks++ }, modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(24.dp), interactionSource = interactions) {
                        Box(Modifier.fillMaxSize().background(Color.White))
                        Box(Modifier.align(Alignment.TopStart).size(16.dp).background(Color.Blue))
                        Box(Modifier.align(Alignment.BottomStart).size(16.dp).background(Color.Red))
                    }
                }
            }
        }.use { scene ->
            var time = System.nanoTime()
            fun frame() = scene.render(time).use { it.toComposeImageBitmap().toPixelMap() }
            fun settle() { repeat(12) { time += 40_000_000L; scene.render(time).close() } }
            settle()
            val before = frame()
            assertEquals(Color.Blue, before[1, 1])
            assertEquals(Color.Red, before[1, 98], "Content at the bottom corner must not be clipped")
            val press = PressInteraction.Press(Offset(80f, 50f))
            interactions.emit(press)
            settle()
            val held = frame()
            assertEquals(before[1, 1], held[1, 1], "Ripple must remain inside the rounded shape")
            assertEquals(before[1, 98], held[1, 98])
            assertTrue(held[80, 50].red < .99f, "The control must still show its pressed feedback")
            interactions.emit(PressInteraction.Release(press))
            settle()
            fun nodes(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::nodes)
            val targets = scene.semanticsOwners.flatMap { nodes(it.unmergedRootSemanticsNode) }
                .filter { it.config.contains(SemanticsActions.OnClick) }
            assertEquals(1, targets.size)
            scene.sendPointerEvent(PointerEventType.Press, Offset(2f, 97f), type = PointerType.Touch)
            scene.sendPointerEvent(PointerEventType.Release, Offset(2f, 97f), type = PointerType.Touch)
            settle()
            assertEquals(1, clicks, "The edge containing the supporting text must stay clickable")
        }
    }
}

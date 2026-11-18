package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.use
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarState
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.ToolbarNestedScrollConnection
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class CollapsingToolbarScrollTest {
    @Test
    fun fractionalDeltasAtBothLimitsLeaveTheExactRemainderForTheList() {
        for (strategy in ScrollStrategy.entries) {
            val toolbar = CollapsingToolbarState().apply { updateBounds(40, 96) }
            val state = CollapsingToolbarScaffoldState(toolbar).apply {
                updateOffsetLimit(if (strategy == ScrollStrategy.ExitUntilCollapsed) 0 else 40)
            }
            val connection = ToolbarNestedScrollConnection(state, strategy, object : FlingBehavior {
                override suspend fun ScrollScope.performFling(initialVelocity: Float) = initialVelocity
            })
            fun pre(y: Float) = connection.onPreScroll(Offset(0f, y), NestedScrollSource.UserInput).y
            fun post(y: Float) = connection.onPostScroll(Offset.Zero, Offset(0f, y), NestedScrollSource.UserInput).y
            val hideable = strategy != ScrollStrategy.ExitUntilCollapsed
            val travel = if (hideable) 96f else 56f
            repeat((travel * 4).toInt()) { assertEquals(-.25f, pre(-.25f)) }
            repeat(8) { assertEquals(0f, pre(-.25f)) }
            assertEquals(40, toolbar.height)
            assertEquals(if (hideable) -40 else 0, state.offsetY)
            assertEquals(Offset.Zero, connection.onPreScroll(Offset(-20f, 0f), NestedScrollSource.UserInput))

            val earlyReveal = when (strategy) {
                ScrollStrategy.EnterAlways -> travel
                ScrollStrategy.EnterAlwaysCollapsed -> 40f
                ScrollStrategy.ExitUntilCollapsed -> 0f
            }
            assertEquals(earlyReveal, pre(100f))
            assertEquals(travel - earlyReveal, post(100f))
            assertEquals(0f, pre(100f))
            assertEquals(0f, post(100f))
            assertEquals(96, toolbar.height)
            assertEquals(0, state.offsetY)
        }
    }

    @Test
    fun shortListSettlesAfterTouchAndItsViewportKeepsTheSameBottom() {
        lateinit var state: CollapsingToolbarScaffoldState
        var bodyBottom = 0f
        var bodyTop = 0f
        ImageComposeScene(width = 390, height = 600) {
            SauceTheme {
                state = remember { CollapsingToolbarScaffoldState(CollapsingToolbarState()) }
                CollapsingToolbarScaffold(
                    Modifier.fillMaxSize(), state, ScrollStrategy.EnterAlwaysCollapsed,
                    toolbar = { Text("Toolbar", Modifier.fillMaxWidth().height(80.dp)) },
                ) {
                    SauceLazyColumn(Modifier.fillMaxSize().onGloballyPositioned {
                        val bounds = it.boundsInRoot()
                        bodyBottom = bounds.bottom
                        bodyTop = bounds.top
                    }) {
                        item { Text("Short list", Modifier.height(96.dp)) }
                    }
                }
            }
        }.use { scene ->
            var time = 1_000_000_000L
            fun frame(frames: Int = 1) {
                repeat(frames) { time += 25_000_000L; scene.render(time).close() }
            }
            fun touch(type: PointerEventType, y: Float) {
                scene.sendPointerEvent(type, Offset(195f, y), timeMillis = time / 1_000_000, type = PointerType.Touch)
                frame()
            }
            fun dragBy(distance: Float) {
                touch(PointerEventType.Press, 320f)
                repeat(9) { index ->
                    touch(PointerEventType.Move, 320f + distance * (index + 1) / 9)
                    assertEquals(600f, bodyBottom)
                }
                frame(8) // Release with no momentum to verify the snap from the intermediate offset.
                touch(PointerEventType.Release, 320f + distance)
            }
            frame(8)
            assertEquals(80f, bodyTop)
            dragBy(-64f)
            assertTrue(state.offsetY in -79..-41)
            frame(30)
            assertEquals(-80, state.offsetY)
            assertEquals(0f, bodyTop)
            dragBy(64f)
            frame(30)
            assertEquals(0, state.offsetY)
            assertEquals(80f, bodyTop)
            assertEquals(600f, bodyBottom)

            dragBy(-64f)
            frame(2)
            assertTrue(state.offsetY < -40)
            dragBy(120f)
            frame(30)
            assertEquals(0, state.offsetY, "A new drag must interrupt the previous release animation")
            assertEquals(80f, bodyTop)
        }
    }
}

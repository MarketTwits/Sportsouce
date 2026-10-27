package com.markettwits.sportsouce.start

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import com.markettwits.sportsouce.start.presentation.album.presentation.components.AlbumOverscrollEffect
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumOverscrollTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun edgesResistPullingReverseWithoutMovingTheGridAndSpringBackAfterRelease() {
        val effect = AlbumOverscrollEffect().apply { viewportHeight = 1000f }
        lateinit var scope: CoroutineScope
        ImageComposeScene(width = 390, height = 900) { scope = rememberCoroutineScope() }.use { scene ->
            var time = System.nanoTime()
            fun frames(count: Int = 12) { repeat(count) { time += 16_000_000L; scene.render(time).close() } }
            frames()
            var calls = 0
            effect.applyToScroll(Offset(0f, -120f), NestedScrollSource.UserInput) { calls++; it }
            assertEquals(1, calls)
            assertEquals(0f, effect.offset, "Ordinary scrolling must not stretch the cover")
            val consumed = effect.applyToScroll(Offset(0f, 200f), NestedScrollSource.UserInput) { calls++; Offset.Zero }
            assertEquals(200f, consumed.y)
            val stretch = effect.offset
            assertTrue(stretch in 1f..199f, "Overscroll must resist rather than track the finger one-to-one")
            effect.applyToScroll(Offset(0f, -60f), NestedScrollSource.UserInput) { assertEquals(0f, it.y); Offset.Zero }
            assertTrue(effect.offset in 1f..stretch)
            var flingCalls = 0
            scope.launch { effect.applyToFling(Velocity(0f, 1200f)) { flingCalls++; assertEquals(0f, it.y); Velocity.Zero } }
            frames(120)
            assertEquals(1, flingCalls)
            assertEquals(0f, effect.offset, .1f)
            assertFalse(effect.isInProgress)
            // The lower edge bounces in the other direction and still settles completely.
            effect.applyToScroll(Offset(0f, -300f), NestedScrollSource.UserInput) { Offset.Zero }
            assertTrue(effect.offset < 0f)
            scope.launch { effect.applyToFling(Velocity.Zero) { Velocity.Zero } }
            frames(120)
            assertEquals(0f, effect.offset, .1f)
        }
    }
}

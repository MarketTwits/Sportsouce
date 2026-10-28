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

    @Test fun inertiaAndFractionalRemaindersCannotStartOverscroll() {
        val effect = AlbumOverscrollEffect().apply { viewportHeight = 1000f }
        var calls = 0
        for (delta in listOf(-600f, 600f)) {
            val consumed = effect.applyToScroll(Offset(0f, delta), NestedScrollSource.SideEffect) { calls++; Offset.Zero }
            assertEquals(0f, consumed.y)
            assertEquals(0f, effect.offset)
            effect.applyToScroll(Offset(0f, delta), NestedScrollSource.UserInput) { calls++; it.copy(y = it.y - .2f) }
            assertEquals(0f, effect.offset, "Subpixel layout rounding must not create a rubber band")
        }
        assertEquals(4, calls)
    }

    @Test fun loadingAndTemporaryPagingEdgesDoNotConsumeTheGesture() {
        val effect = AlbumOverscrollEffect().apply { viewportHeight = 1000f }
        // Neither edge is final while refreshing. An unfinished append disables only the bottom.
        effect.updateBounds { false }
        for (delta in listOf(-300f, 300f)) {
            assertEquals(Offset.Zero, effect.applyToScroll(Offset(0f, delta), NestedScrollSource.UserInput) { Offset.Zero })
            assertEquals(0f, effect.offset)
        }
        effect.updateBounds { it > 0f }
        effect.applyToScroll(Offset(0f, -300f), NestedScrollSource.UserInput) { Offset.Zero }
        assertEquals(0f, effect.offset)
        var forwarded = Offset.Zero
        effect.applyToScroll(Offset(0f, 100f), NestedScrollSource.UserInput) { forwarded = it; it }
        assertEquals(100f, forwarded.y, "Scrolling back from a loading footer must not repay false tension")
        effect.applyToScroll(Offset(0f, 200f), NestedScrollSource.UserInput) { Offset.Zero }
        assertTrue(effect.offset > 0f, "The genuine loaded top edge still stretches")
    }

    @Test fun aChangedBoundaryDropsOldTensionBeforeForwardingTheNextScroll() {
        val effect = AlbumOverscrollEffect().apply { viewportHeight = 1000f }
        var atEnd = true
        effect.updateBounds { it < 0f && atEnd }
        effect.applyToScroll(Offset(0f, -200f), NestedScrollSource.UserInput) { Offset.Zero }
        assertTrue(effect.offset < 0f)
        atEnd = false // A new page has expanded the layout without another pointer event yet.
        assertEquals(0f, effect.offset)
        var forwarded = Offset.Zero
        effect.applyToScroll(Offset(0f, 120f), NestedScrollSource.UserInput) { forwarded = it; it }
        assertEquals(120f, forwarded.y)
        assertFalse(effect.isInProgress)
        effect.updateBounds { true }
        assertEquals(0f, effect.offset, "Old tension cannot return when a different edge becomes available")
    }

    @Test fun cancellingAReleaseCannotLeaveTheGridDisplaced() {
        val effect = AlbumOverscrollEffect().apply { viewportHeight = 1000f }
        lateinit var scope: CoroutineScope
        ImageComposeScene(width = 390, height = 900) { scope = rememberCoroutineScope() }.use { scene ->
            var time = System.nanoTime()
            fun frames(count: Int) { repeat(count) { time += 16_000_000L; scene.render(time).close() } }
            frames(2)
            effect.applyToScroll(Offset(0f, 200f), NestedScrollSource.UserInput) { Offset.Zero }
            val releasing = scope.launch { effect.applyToFling(Velocity.Zero) { awaitCancellation() } }
            frames(2)
            releasing.cancel()
            frames(2)
            assertEquals(0f, effect.offset, "Cancelling performFling must clear the displacement too")
            effect.applyToScroll(Offset(0f, 200f), NestedScrollSource.UserInput) { Offset.Zero }
            val spring = scope.launch { effect.applyToFling(Velocity.Zero) { Velocity.Zero } }
            frames(2)
            assertTrue(effect.offset > 0f)
            spring.cancel()
            frames(2)
            assertEquals(0f, effect.offset)
            assertFalse(effect.isInProgress)
            effect.applyToScroll(Offset(0f, 200f), NestedScrollSource.UserInput) { Offset.Zero }
            scope.launch { effect.applyToFling(Velocity.Zero) { Velocity.Zero } }
            frames(2)
            effect.updateBounds { false }
            effect.updateBounds { true }
            frames(4)
            assertEquals(0f, effect.offset, "A page/filter change cannot resurrect a superseded spring")
            frames(120)
        }
    }
}

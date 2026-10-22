package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Stable
internal class GuestProfileParallax {
    var dragOffset by mutableFloatStateOf(0f)
    var enabled by mutableStateOf(true)
    fun offset(scroll: ScrollState, limit: Float): Float =
        if (enabled) (-scroll.value * .16f + dragOffset).coerceIn(-limit, limit) else 0f
}

/** Observe scroll gestures without consuming them, preserving scrolling, clicks and system back. */
@Composable
internal fun rememberGuestProfileParallax(scroll: ScrollState, limit: Float): Pair<GuestProfileParallax, NestedScrollConnection> {
    val state = remember { GuestProfileParallax() }
    val scope = rememberCoroutineScope()
    var settle by remember { mutableStateOf<Job?>(null) }
    LaunchedEffect(state) {
        val durationScale = coroutineContext[MotionDurationScale]
        snapshotFlow { durationScale?.scaleFactor ?: 1f }.collect { scale ->
            state.enabled = scale > 0f
            if (!state.enabled) { settle?.cancel(); state.dragOffset = 0f }
        }
    }
    val connection = remember(scroll, limit) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && state.enabled) {
                    settle?.cancel()
                    // A short drag can still move the photograph when all content already fits.
                    if (scroll.maxValue == 0) state.dragOffset =
                        (state.dragOffset + available.y * .16f).coerceIn(-limit, limit)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                settle?.cancel()
                if (state.dragOffset != 0f) settle = scope.launch {
                    animate(state.dragOffset, 0f, animationSpec = spring(dampingRatio = .9f, stiffness = 240f)) { value, _ ->
                        state.dragOffset = value
                    }
                }
                return Velocity.Zero
            }
        }
    }
    return state to connection
}

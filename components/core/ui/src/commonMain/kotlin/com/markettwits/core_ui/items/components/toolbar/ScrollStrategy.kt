/*
 * Copyright (c) 2021 onebone <me@onebone.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.markettwits.core_ui.items.components.toolbar

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class ScrollStrategy {
    EnterAlways,
    EnterAlwaysCollapsed,
    ExitUntilCollapsed,
}

internal class ToolbarNestedScrollConnection(
    private val state: CollapsingToolbarScaffoldState,
    private val strategy: ScrollStrategy,
    private val flingBehavior: FlingBehavior,
) : NestedScrollConnection {
    private val toolbar get() = state.toolbarState
    private var settleJob: Job? = null
    private var movedByScroll = false

    fun cancelSettling() {
        settleJob?.cancel()
        movedByScroll = false
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y
        if (dy == 0f) return Offset.Zero
        settleJob?.cancel()
        val moved = when {
            dy < 0f || strategy == ScrollStrategy.EnterAlways -> moveToolbar(dy)
            strategy == ScrollStrategy.EnterAlwaysCollapsed -> state.dispatchOffsetDelta(dy)
            else -> 0f
        }
        if (moved == 0f) return Offset.Zero
        movedByScroll = true
        return Offset(0f, moved)
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (available.y <= 0f || strategy == ScrollStrategy.EnterAlways) return Offset.Zero
        val moved = moveToolbar(available.y)
        if (moved == 0f) return Offset.Zero
        movedByScroll = true
        return Offset(0f, moved)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (!movedByScroll && abs(available.y) <= 1f) return Velocity.Zero
        movedByScroll = false
        settleJob?.cancel()
        var velocityUsed = 0f
        coroutineScope {
            val animation = launch(start = CoroutineStart.LAZY) {
                // Keep the child's fling continuous through pre-scroll. Only run a separate
                // decay when the child reaches an edge and hands back vertical velocity.
                if (abs(available.y) > 1f && canMove(available.y)) {
                    val scope = object : ScrollScope {
                        override fun scrollBy(pixels: Float) = moveToolbar(pixels)
                    }
                    val remaining = with(flingBehavior) { scope.performFling(available.y) }
                    velocityUsed = available.y - remaining.coerceIn(minOf(0f, available.y), maxOf(0f, available.y))
                }
                snap()
            }
            settleJob = animation
            try {
                animation.join()
            } finally {
                if (settleJob === animation) settleJob = null
            }
        }
        return Velocity(0f, velocityUsed)
    }

    private fun canMove(dy: Float): Boolean =
        if (dy > 0f) state.hiddenOffset < 0f || toolbar.heightPx < toolbar.maxHeight
        else toolbar.heightPx > toolbar.minHeight ||
            (strategy != ScrollStrategy.ExitUntilCollapsed && state.hiddenOffset > -toolbar.minHeight)

    private fun moveToolbar(dy: Float): Float {
        if (strategy == ScrollStrategy.ExitUntilCollapsed) return toolbar.dispatchRawDelta(dy)
        return if (dy < 0f) {
            val resized = toolbar.dispatchRawDelta(dy)
            resized + state.dispatchOffsetDelta(dy - resized)
        } else {
            val revealed = state.dispatchOffsetDelta(dy)
            revealed + toolbar.dispatchRawDelta(dy - revealed)
        }
    }

    private suspend fun snap() {
        val current: Float
        val lower: Float
        val upper: Float
        if (strategy != ScrollStrategy.ExitUntilCollapsed && state.hiddenOffset < 0f) {
            current = state.hiddenOffset
            lower = -toolbar.minHeight.toFloat()
            upper = 0f
        } else {
            current = toolbar.heightPx
            lower = toolbar.minHeight.toFloat()
            upper = toolbar.maxHeight.toFloat()
        }
        if (current <= lower || current >= upper) return

        val distance = (if (current - lower < upper - current) lower else upper) - current
        var previous = 0f
        AnimationState(0f).animateTo(distance, tween(180)) {
            moveToolbar(value - previous)
            previous = value
        }
    }
}

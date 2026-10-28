package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sign

/** Bouncing gallery physics: resist the pull, stretch the cover and spring back on release. */
@Stable
internal class AlbumOverscrollEffect : OverscrollEffect {
    private var pull by mutableFloatStateOf(0f)
    private var revision = 0
    var viewportHeight: Float = 1f
    private var canPull: (Float) -> Boolean = { true }

    fun updateBounds(canPull: (Float) -> Boolean) {
        this.canPull = canPull
        discardInvalidPull()
    }

    private fun discardInvalidPull() {
        if (abs(pull) <= .5f || !canPull(pull)) clearPull()
    }

    private fun clearPull() {
        if (pull != 0f) { pull = 0f; revision++ }
    }

    val offset: Float get() {
        if (!canPull(pull)) return 0f
        val limit = (viewportHeight * .4f).coerceAtLeast(1f)
        return sign(pull) * limit * (1f - exp(-abs(pull) * .52f / limit))
    }
    override val isInProgress: Boolean get() = abs(pull) > .5f && canPull(pull)
    override val node = object : Modifier.Node() {}

    override fun applyToScroll(delta: Offset, source: NestedScrollSource, performScroll: (Offset) -> Offset): Offset {
        discardInvalidPull()
        // Fling, programmatic scrolling and paging remeasurement must never start a rubber band.
        if (source != NestedScrollSource.UserInput) return performScroll(delta)
        var released = 0f
        if (pull != 0f && pull * delta.y < 0f) {
            released = sign(delta.y) * minOf(abs(pull), abs(delta.y))
            pull += released
        }
        val consumed = performScroll(delta.copy(y = delta.y - released))
        val remaining = delta.y - released - consumed.y
        val accepted = if (abs(remaining) > .5f && canPull(remaining)) remaining else 0f
        if (accepted != 0f) pull = (pull + accepted).coerceIn(-viewportHeight * 2f, viewportHeight * 2f)
        return Offset(consumed.x, consumed.y + released + accepted)
    }

    override suspend fun applyToFling(velocity: Velocity, performFling: suspend (Velocity) -> Velocity) {
        try {
            discardInvalidPull()
            val wasPulling = isInProgress
            val consumed = performFling(if (wasPulling) velocity.copy(y = 0f) else velocity)
            if (isInProgress) {
                val speed = if (wasPulling) velocity.y else velocity.y - consumed.y
                val direction = sign(pull)
                val springRevision = revision
                animate(pull, 0f, initialVelocity = speed * .52f,
                    animationSpec = spring(dampingRatio = .86f, stiffness = 180f)) { value, _ ->
                    // An appended page can turn the old edge into normal scrollable content mid-spring.
                    if (springRevision == revision) {
                        if (canPull(direction)) pull = direction * (value * direction).coerceAtLeast(0f)
                        else clearPull()
                    }
                }
            }
        } finally {
            // A new gesture, filter or scrollToItem may cancel a fling before its spring completes.
            clearPull()
        }
    }
}

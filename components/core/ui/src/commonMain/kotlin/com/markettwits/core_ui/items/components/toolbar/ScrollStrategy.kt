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

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

enum class ScrollStrategy {
	EnterAlways {
		override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior
		): NestedScrollConnection =
			EnterAlwaysNestedScrollConnection(offsetY, toolbarState, flingBehavior)
	},
	EnterAlwaysCollapsed {
		override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior
		): NestedScrollConnection =
			EnterAlwaysCollapsedNestedScrollConnection(offsetY, toolbarState, flingBehavior)
	},
	ExitUntilCollapsed {
		override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior
		): NestedScrollConnection =
			ExitUntilCollapsedNestedScrollConnection(toolbarState, flingBehavior)
	};

	internal abstract fun create(
        offsetY: MutableState<Int>,
        toolbarState: CollapsingToolbarState,
        flingBehavior: FlingBehavior
	): NestedScrollConnection
}

private class ScrollDelegate(
	private val offsetY: MutableState<Int>
) {
	private var scrollToBeConsumed: Float = 0f

	fun doScroll(delta: Float) {
		val scroll = scrollToBeConsumed + delta
		val scrollInt = scroll.toInt()

		scrollToBeConsumed = scroll - scrollInt

		offsetY.value += scrollInt
	}
}

internal class EnterAlwaysNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior
) : NestedScrollConnection {
	private val scrollDelegate = ScrollDelegate(offsetY)
	//private val tracker = RelativeVelocityTracker(CurrentTimeProviderImpl())

	override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
		val dy = available.y

		val toolbar = toolbarState.height.toFloat()
		val offset = offsetY.value.toFloat()

		// -toolbarHeight <= offsetY + dy <= 0
		val consume = if (dy < 0) {
			val toolbarConsumption = toolbarState.dispatchRawDelta(dy)
			val remaining = dy - toolbarConsumption
			val offsetConsumption = remaining.coerceAtLeast(-toolbar - offset)
			scrollDelegate.doScroll(offsetConsumption)

			toolbarConsumption + offsetConsumption
		} else {
			val offsetConsumption = dy.coerceAtMost(-offset)
			scrollDelegate.doScroll(offsetConsumption)

			val toolbarConsumption = toolbarState.dispatchRawDelta(dy - offsetConsumption)

			offsetConsumption + toolbarConsumption
		}

		return Offset(0f, consume)
	}

	override suspend fun onPreFling(available: Velocity): Velocity {
		val left = if (available.y > 0) {
			toolbarState.fling(flingBehavior, available.y)
		} else {
			// If velocity < 0, the main content should have a remaining scroll space
			// so the scroll resumes to the onPreScroll(..., Fling) phase. Hence we do
			// not need to process it at onPostFling() manually.
			available.y
		}

		return Velocity(x = 0f, y = available.y - left)
	}
}

internal class EnterAlwaysCollapsedNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior
) : NestedScrollConnection {
	private val scrollDelegate = ScrollDelegate(offsetY)
	//private val tracker = RelativeVelocityTracker(CurrentTimeProviderImpl())

	override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
		val dy = available.y

		val consumed = if (dy > 0) { // expanding: offset -> body -> toolbar
			val offsetConsumption = dy.coerceAtMost(-offsetY.value.toFloat())
			scrollDelegate.doScroll(offsetConsumption)

			offsetConsumption
		} else { // collapsing: toolbar -> offset -> body
			val toolbarConsumption = toolbarState.dispatchRawDelta(dy)
			val offsetConsumption =
				(dy - toolbarConsumption).coerceAtLeast(-toolbarState.height.toFloat() - offsetY.value)

			scrollDelegate.doScroll(offsetConsumption)

			toolbarConsumption + offsetConsumption
		}

		return Offset(0f, consumed)
	}

	override fun onPostScroll(
		consumed: Offset,
		available: Offset,
		source: NestedScrollSource
	): Offset {
		val dy = available.y

		return if (dy > 0) {
			Offset(0f, toolbarState.dispatchRawDelta(dy))
		} else {
			Offset(0f, 0f)
		}
	}

	override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
		val dy = available.y

		val left = if (dy > 0) {
			// onPostFling() has positive available scroll value only called if the main scroll
			// has leftover scroll, i.e. the scroll of the main content has done. So we just process
			// fling if the available value is positive.
			toolbarState.fling(flingBehavior, dy)
		} else {
			dy
		}

		return Velocity(x = 0f, y = available.y - left)
	}
}

internal class ExitUntilCollapsedNestedScrollConnection(
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior
) : NestedScrollConnection {
	override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
		val dy = available.y

		val consume = if (dy < 0) { // collapsing: toolbar -> body
			toolbarState.dispatchRawDelta(dy)
		} else {
			0f
		}

		return Offset(0f, consume)
	}

	override fun onPostScroll(
		consumed: Offset,
		available: Offset,
		source: NestedScrollSource
	): Offset {
		val dy = available.y

		val consume = if (dy > 0) { // expanding: body -> toolbar
			toolbarState.dispatchRawDelta(dy)
		} else {
			0f
		}

		return Offset(0f, consume)
	}

	override suspend fun onPreFling(available: Velocity): Velocity {
		val left = if (available.y < 0) {
			toolbarState.fling(flingBehavior, available.y)
		} else {
			available.y
		}

		return Velocity(x = 0f, y = available.y - left)
	}

	override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
		val velocity = available.y

		val left = if (velocity > 0) {
			toolbarState.fling(flingBehavior, velocity)
		} else {
			velocity
		}

		return Velocity(x = 0f, y = available.y - left)
	}
}

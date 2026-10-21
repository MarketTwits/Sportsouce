package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import kotlinx.coroutines.*

/** Reversing the drag restores selections outside this gesture's current range. */
internal fun albumSelectionRange(photos: List<AlbumPhoto>, baseline: Map<Int, AlbumPhoto>, anchor: Int, end: Int, selecting: Boolean): Map<Int, AlbumPhoto> {
    val a = photos.indexOfFirst { it.id == anchor }; val b = photos.indexOfFirst { it.id == end }
    if (a < 0 || b < 0) return baseline
    val range = photos.subList(minOf(a, b), maxOf(a, b) + 1).associateBy { it.id }
    return if (selecting) baseline + range else baseline - range.keys
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Modifier.albumSelectionGesture(
    state: LazyGridState, sources: AlbumPhotoSources, photos: List<AlbumPhoto>, selected: Map<Int, AlbumPhoto>, enabled: Boolean = true,
    onSelection: (List<AlbumPhoto>) -> Unit,
): Modifier {
    val currentPhotos by rememberUpdatedState(photos)
    val currentSelection by rememberUpdatedState(selected)
    val update by rememberUpdatedState(onSelection)
    val haptic = LocalHapticFeedback.current
    val selectedIds = remember(selected) { selected.keys.toSet() }
    var previousSelection by remember { mutableStateOf(selectedIds) }
    LaunchedEffect(selectedIds) {
        if (previousSelection != selectedIds) {
            previousSelection = selectedIds
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val viewport = remember { AlbumSelectionViewport() }
    return onGloballyPositioned { viewport.origin = it.positionInWindow(); viewport.height = it.size.height.toFloat() }
        .pointerInput(state, enabled, sources, rtl) {
        if (!enabled) return@pointerInput
        coroutineScope {
            val gestureScope = this
            fun photoAt(position: Offset, clamp: Boolean = false): Int? {
                val bottom = (viewport.height - state.layoutInfo.afterContentPadding - 1).coerceAtLeast(1f)
                val point = Offset(position.x, if (clamp) position.y.coerceIn(1f, bottom) else position.y) + viewport.origin
                return sources.indexAt(point)?.let { currentPhotos.getOrNull(it)?.id }
            }
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                if (down.isConsumed) return@awaitEachGesture
                val anchor = photoAt(down.position) ?: return@awaitEachGesture
                val point = down.position + viewport.origin
                val tile = sources.indexAt(point)?.let(sources::boundsAt)
                val handleSize = 40.dp.toPx()
                val fromHandle = currentSelection.isNotEmpty() && tile != null &&
                    point.y < tile.top + handleSize &&
                    (if (rtl) point.x < tile.left + handleSize else point.x >= tile.right - handleSize)
                // Only the visible selection handle can start a range immediately. Elsewhere,
                // wait for long press and let LazyVerticalGrid consume ordinary scroll swipes.
                val press = if (fromHandle) down else {
                    awaitPointerEvent(PointerEventPass.Final)
                    awaitLongPressOrCancellation(down.id) ?: return@awaitEachGesture
                }
                val baseline = currentSelection.toMap()
                press.consume()
                var position = press.position
                val selecting = anchor !in baseline
                var last: Int? = null
                fun select(id: Int?) {
                    if (id != null && id != last) {
                        update(albumSelectionRange(currentPhotos, baseline, anchor, id, selecting).values.toList())
                        last = id
                    }
                }
                select(anchor)
                val autoScroll = gestureScope.launch {
                    var previous = 0L
                    while (isActive) {
                        val now = withFrameNanos { it }
                        val seconds = if (previous == 0L) 0f else ((now - previous) / 1_000_000_000f).coerceAtMost(.05f)
                        previous = now
                        val edge = 56 * density
                        val bottom = viewport.height - state.layoutInfo.afterContentPadding
                        val speed = when {
                            position.y < edge -> -((edge - position.y) / edge).coerceIn(0f, 1f)
                            position.y > bottom - edge -> ((position.y - bottom + edge) / edge).coerceIn(0f, 1f)
                            else -> 0f
                        }
                        if (speed != 0f) {
                            state.scrollBy(speed * 800 * density * seconds)
                            select(photoAt(position, true))
                        }
                    }
                }
                try {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.changes.count { it.pressed } > 1) break
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        position = change.position
                        select(photoAt(position, true))
                        change.consume()
                        if (!change.pressed) break
                    }
                } finally { autoScroll.cancel() }
            }
        }
    }
}

private class AlbumSelectionViewport {
    var origin = Offset.Zero
    var height = 0f
}

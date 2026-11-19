package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import com.markettwits.core_ui.items.components.list.SauceScrollbar
import com.markettwits.core_ui.items.components.list.SauceScrollbarMetrics
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.math.roundToInt

/** Exact pixel/index conversion for full-span album headings followed by square photo rows. */
internal data class AlbumScrollLayout(
    val headers: List<Float>,
    val photoCount: Int,
    val columns: Int,
    val rowHeight: Float,
    val spacing: Float,
    val beforePadding: Float,
    val afterPadding: Float,
    val footers: List<Float> = emptyList(),
) {
    private val rows get() = (photoCount + columns - 1) / columns
    private val photoStart get() = headers.sum() + headers.size * spacing
    private val photoHeight get() = rows * rowHeight + (rows - 1).coerceAtLeast(0) * spacing
    private val footerStart get() = photoStart + photoHeight + if (rows > 0 && footers.isNotEmpty()) spacing else 0f
    val contentHeight get() = beforePadding + footerStart + footers.sum() + (footers.size - 1).coerceAtLeast(0) * spacing + afterPadding

    fun offset(index: Int, scrollOffset: Int): Float = when {
        index < headers.size -> headers.take(index).sum() + index * spacing
        index < headers.size + photoCount -> photoStart + (index - headers.size) / columns * (rowHeight + spacing)
        else -> footerStart + footers.take(index - headers.size - photoCount).sum() + (index - headers.size - photoCount) * spacing
    } + scrollOffset

    fun position(offset: Float): Pair<Int, Int> {
        var remaining = offset.coerceAtLeast(0f)
        for ((index, height) in headers.withIndex()) {
            if (remaining < height + spacing) return index to remaining.roundToInt()
            remaining -= height + spacing
        }
        if (photoCount == 0) return 0 to 0
        val row = (remaining / (rowHeight + spacing).coerceAtLeast(1f)).toInt().coerceIn(0, (rows - 1).coerceAtLeast(0))
        return (headers.size + row * columns) to (remaining - row * (rowHeight + spacing)).coerceAtLeast(0f).roundToInt()
    }
}

private data class AlbumScrollRequest(val target: Float, val release: Boolean = false)

@Composable
internal fun AlbumScrollbar(
    grid: LazyGridState,
    layout: AlbumScrollLayout,
    overscroll: AlbumOverscrollEffect,
    viewportHeight: Float,
    topPadding: Dp,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val requests = remember(grid) { Channel<AlbumScrollRequest>(Channel.CONFLATED) }
    var dragging by remember(grid) { mutableStateOf(false) }
    var lastTarget by remember(grid) { mutableFloatStateOf(0f) }
    val currentLayout by rememberUpdatedState(layout)
    val currentViewport by rememberUpdatedState(viewportHeight)
    val metrics = {
        val total = currentLayout.contentHeight
        val maxOffset = (total - currentViewport).coerceAtLeast(0f)
        val offset = when {
            !grid.canScrollBackward -> 0f
            !grid.canScrollForward -> maxOffset
            else -> currentLayout.offset(grid.firstVisibleItemIndex, grid.firstVisibleItemScrollOffset).coerceIn(0f, maxOffset)
        }
        SauceScrollbarMetrics(currentViewport, total, offset, overscroll.offset)
    }
    SideEffect {
        // A distant seek can reach a placeholder edge before its final page arrives.
        // Adopt the real boundary while the finger is still held, without requiring another move.
        if (dragging) {
            val maxOffset = (currentLayout.contentHeight - currentViewport).coerceAtLeast(0f)
            overscroll.pullFromScrollbar(lastTarget.coerceIn(0f, maxOffset) - lastTarget)
        }
    }
    LaunchedEffect(grid) {
        requests.receiveAsFlow().collectLatest { request ->
            val target = request.target
            val maxOffset = (currentLayout.contentHeight - currentViewport).coerceAtLeast(0f)
            val clamped = target.coerceIn(0f, maxOffset)
            if (clamped >= maxOffset && maxOffset > 0f) grid.scrollToItem((grid.layoutInfo.totalItemsCount - 1).coerceAtLeast(0))
            else {
                val (index, offset) = currentLayout.position(clamped)
                grid.scrollToItem(index.coerceIn(0, (grid.layoutInfo.totalItemsCount - 1).coerceAtLeast(0)), offset)
            }
            overscroll.pullFromScrollbar(clamped - target)
            if (request.release) overscroll.applyToFling(Velocity.Zero) { Velocity.Zero }
        }
    }
    SauceScrollbar(metrics, { grid.isScrollInProgress }, { lastTarget = it; requests.trySend(AlbumScrollRequest(it)) }, modifier,
        topPadding = topPadding, bottomPadding = bottomPadding, enabled = enabled,
        description = "Прокрутка фотоальбома",
        onDragStarted = { dragging = true; lastTarget = metrics().offset; requests.trySend(AlbumScrollRequest(lastTarget)) },
        onDragStopped = { dragging = false; requests.trySend(AlbumScrollRequest(lastTarget, release = true)) })
}

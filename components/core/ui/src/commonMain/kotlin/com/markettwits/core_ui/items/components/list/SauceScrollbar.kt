package com.markettwits.core_ui.items.components.list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Immutable
data class SauceScrollbarMetrics(
    val viewport: Float = 0f,
    val content: Float = 0f,
    val offset: Float = 0f,
    /** The gallery's displayed rubber-band translation, positive at the start. */
    val overscroll: Float = 0f,
) {
    val maxOffset: Float get() = (content - viewport).coerceAtLeast(0f)
}

@Immutable
internal data class SauceScrollbarThumb(val top: Float, val length: Float, val travel: Float)

/** Fixed-size thumb; a rubber band compresses it towards the edge without moving outside the track. */
internal fun sauceScrollbarThumb(metrics: SauceScrollbarMetrics, start: Float, end: Float, thumbLength: Float, minOverscrollLength: Float): SauceScrollbarThumb {
    val track = (end - start).coerceAtLeast(0f)
    val viewport = metrics.viewport.coerceAtLeast(1f)
    val resting = min(track, thumbLength)
    val minimum = min(resting, minOverscrollLength)
    val compression = (abs(metrics.overscroll) / viewport / .2f).coerceIn(0f, 1f)
    val length = max(minimum, resting * (1f - compression))
    val travel = (track - length).coerceAtLeast(0f)
    val progress = if (metrics.maxOffset > 0f) (metrics.offset / metrics.maxOffset).coerceIn(0f, 1f) else 0f
    return SauceScrollbarThumb(start + travel * progress, length, travel)
}

/** Thumb-only gallery scrollbar. Its invisible track never steals taps or ordinary photo swipes. */
@Composable
fun SauceScrollbar(
    metrics: () -> SauceScrollbarMetrics,
    isScrolling: () -> Boolean,
    onScrollTo: (Float) -> Unit,
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    enabled: Boolean = true,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
    description: String = "Прокрутка списка",
) {
    val density = LocalDensity.current
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val colors = SauceTheme.colors
    val opacity = remember { Animatable(0f) }
    var dragging by remember { mutableStateOf(false) }
    val currentMetrics by rememberUpdatedState(metrics)
    val scrolling by rememberUpdatedState(isScrolling)
    val seek by rememberUpdatedState(onScrollTo)
    val started by rememberUpdatedState(onDragStarted)
    val stopped by rememberUpdatedState(onDragStopped)
    val eligible by remember(enabled) { derivedStateOf {
        val m = currentMetrics()
        enabled && m.viewport > 0f && m.maxOffset > .5f
    } }
    LaunchedEffect(enabled) {
        snapshotFlow {
            val m = currentMetrics()
            listOf(eligible, dragging, scrolling(), m.offset, m.overscroll)
        }.collectLatest {
            if (!eligible) opacity.snapTo(0f)
            else {
                opacity.snapTo(1f)
                if (!dragging && !scrolling() && abs(currentMetrics().overscroll) < .1f) {
                    delay(2000)
                    opacity.animateTo(0f, tween(300))
                }
            }
        }
    }
    BoxWithConstraints(modifier) {
        val start = with(density) { topPadding.toPx() }
        val end = with(density) { (maxHeight - bottomPadding).toPx() }.coerceAtLeast(start)
        val thumbLength = with(density) { 36.dp.toPx() }
        val minOver = with(density) { 18.dp.toPx() }
        val geometry by remember(start, end, thumbLength, minOver) { derivedStateOf {
            sauceScrollbarThumb(currentMetrics(), start, end, thumbLength, minOver)
        } }
        if (eligible) {
            Canvas(Modifier.matchParentSize()) {
                val thumb = geometry
                val width = 8.dp.toPx()
                drawRoundRect(
                    color = (if (dragging) colors.textSecondary else colors.controlBorder).copy(alpha = opacity.value * .95f),
                    topLeft = Offset(if (rtl) 3.dp.toPx() else size.width - width - 3.dp.toPx(), thumb.top),
                    size = Size(width, thumb.length), cornerRadius = CornerRadius(4.dp.toPx()),
                )
            }
            if (opacity.value > .01f || dragging) {
                val touchHeight = maxOf(geometry.length, with(density) { 48.dp.toPx() }).coerceAtMost((end - start).coerceAtLeast(0f))
                val touchTop = (geometry.top - (touchHeight - geometry.length) / 2f).coerceIn(start, (end - touchHeight).coerceAtLeast(start))
                var origin by remember { mutableStateOf(Offset.Zero) }
                Box(Modifier.align(Alignment.TopEnd).width(48.dp).height(with(density) { touchHeight.toDp() })
                    .offset { IntOffset(0, touchTop.roundToInt()) }
                    .onGloballyPositioned { origin = it.positionInRoot() }
                    .semantics {
                        contentDescription = description
                        val m = currentMetrics()
                        progressBarRangeInfo = ProgressBarRangeInfo(if (m.maxOffset > 0f) (m.offset / m.maxOffset).coerceIn(0f, 1f) else 0f, 0f..1f)
                        setProgress { value -> seek(value.coerceIn(0f, 1f) * currentMetrics().maxOffset); true }
                    }.focusable()
                    .pointerInput(start, end, enabled) {
                        awaitEachGesture {
                            val down = awaitFirstDown(pass = PointerEventPass.Initial)
                            down.consume()
                            val downY = origin.y + down.position.y
                            val initial = currentMetrics()
                            val travel = geometry.travel.coerceAtLeast(1f)
                            dragging = true
                            started()
                            try {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                    if (event.changes.count { it.pressed } > 1) break
                                    val delta = origin.y + change.position.y - downY
                                    seek(initial.offset + delta * initial.maxOffset / travel)
                                    change.consume()
                                    if (!change.pressed) break
                                }
                            } finally {
                                dragging = false
                                stopped()
                            }
                        }
                    })
            }
        }
    }
}

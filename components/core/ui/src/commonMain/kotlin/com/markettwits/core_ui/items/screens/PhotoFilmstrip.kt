package com.markettwits.core_ui.items.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

/** Pager motion follows continuously; a user drag owns the strip through its entire fling. */
@Composable
internal fun PhotoFilmstrip(
    images: List<String>,
    selected: Int,
    position: () -> Float = { selected.toFloat() },
    onPreview: (Int?) -> Unit = {},
    onSelect: (Int) -> Unit,
) {
    if (images.isEmpty()) return
    val scope = rememberCoroutineScope()
    val state = rememberLazyGridState(initialFirstVisibleItemIndex = selected.coerceIn(images.indices))
    val dragged by state.interactionSource.collectIsDraggedAsState()
    var touching by remember { mutableStateOf(false) }
    var scrubbing by remember { mutableStateOf(false) }
    val currentPosition by rememberUpdatedState(position)
    val preview by rememberUpdatedState(onPreview)
    val select by rememberUpdatedState(onSelect)
    val count by rememberUpdatedState(images.size)
    val colors = SauceTheme.colors
    val step = with(LocalDensity.current) { (52.dp.roundToPx() + 6.dp.roundToPx()).toFloat() }
    val stripPosition by remember(state, step) { derivedStateOf { state.filmstripPosition(step) } }
    val focused = if (scrubbing) stripPosition.roundToInt().coerceIn(images.indices) else selected

    LaunchedEffect(state, step) {
        snapshotFlow { StripInteraction(dragged, touching, state.isScrollInProgress, stripPosition.roundToInt()) }
            .collect { interaction ->
                if (interaction.dragged) scrubbing = true
                if (scrubbing) {
                    val target = interaction.index.coerceIn(0, count - 1)
                    preview(target)
                    if (!interaction.dragged && !interaction.touching && !interaction.scrolling) {
                        scrubbing = false
                        select(target)
                    }
                }
            }
    }
    LaunchedEffect(state, step, touching, dragged, scrubbing) {
        if (!touching && !dragged && !scrubbing) {
            snapshotFlow { currentPosition().coerceIn(0f, (count - 1).toFloat()) }.collectLatest { target ->
                val index = floor(target).toInt()
                state.scrollToItem(index, ((target - index) * step).roundToInt())
            }
        }
    }
    DisposableEffect(Unit) { onDispose { preview(null) } }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val sidePadding = ((maxWidth - 52.dp) / 2).coerceAtLeast(0.dp)
        LazyHorizontalGrid(rows = GridCells.Fixed(1), state = state,
            modifier = Modifier.fillMaxWidth().height(64.dp)
                .semantics { contentDescription = "Лента миниатюр" }
                .pointerInput(Unit) {
                    try {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                            touching = true
                            do {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                            } while (event.changes.any { it.pressed })
                            touching = false
                        }
                    } finally { touching = false }
                },
            contentPadding = PaddingValues(horizontal = sidePadding), horizontalArrangement = Arrangement.spacedBy(6.dp),
            flingBehavior = rememberPhotoFilmstripFling(state, step)) {
            items(images.size, key = { it }) { index ->
                Box(Modifier.width(52.dp).fillMaxHeight().clip(SauceShapes.badge)
                    .clickable(role = Role.Button) {
                        scrubbing = false
                        scope.launch {
                            state.stopScroll()
                            select(index)
                        }
                    }
                    .semantics { contentDescription = "Миниатюра ${index + 1}"; this.selected = index == focused }) {
                    Box(Modifier.fillMaxSize().graphicsLayer {
                        val proximity = (1f - abs(index - if (scrubbing) stripPosition else currentPosition())).coerceIn(0f, 1f)
                        scaleX = .88f + .12f * proximity
                        scaleY = .88f + .12f * proximity
                        alpha = .68f + .32f * proximity
                    }.clip(SauceShapes.badge)
                        .border(if (index == focused) 2.dp else 0.dp,
                            if (index == focused) colors.action else colors.border.copy(alpha = 0f), SauceShapes.badge).padding(3.dp)) {
                        AlbumImage(images[index], AlbumImageSize.Filmstrip,
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().clip(SauceShapes.badge).background(colors.surfaceRaised),
                            loading = { Box(Modifier.fillMaxSize().background(colors.surfaceRaised)) }, error = { _, _ -> })
                    }
                }
            }
        }
    }
}

private data class StripInteraction(val dragged: Boolean, val touching: Boolean, val scrolling: Boolean, val index: Int)

private fun LazyGridState.filmstripPosition(step: Float): Float = firstVisibleItemIndex + firstVisibleItemScrollOffset / step

/** Bounded, gentle coasting followed by centering, all inside the same scroll mutation. */
@Composable
private fun rememberPhotoFilmstripFling(state: LazyGridState, step: Float): FlingBehavior {
    val density = LocalDensity.current
    return remember(state, step, density) {
        val maxVelocity = with(density) { 800.dp.toPx() }
        val decay = exponentialDecay<Float>(frictionMultiplier = .85f, absVelocityThreshold = with(density) { 8.dp.toPx() })
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                var previous = 0f
                AnimationState(initialValue = 0f, initialVelocity = initialVelocity.coerceIn(-maxVelocity, maxVelocity)).animateDecay(decay) {
                    val delta = value - previous
                    val consumed = scrollBy(delta)
                    previous = value
                    if (abs(delta - consumed) > .5f) cancelAnimation()
                }
                val position = state.filmstripPosition(step)
                val target = position.roundToInt().coerceIn(0, (state.layoutInfo.totalItemsCount - 1).coerceAtLeast(0))
                val distance = (target - position) * step
                previous = 0f
                AnimationState(0f).animateTo(distance, tween(160)) {
                    val delta = value - previous
                    val consumed = scrollBy(delta)
                    previous = value
                    if (abs(delta - consumed) > .5f) cancelAnimation()
                }
                return 0f
            }
        }
    }
}

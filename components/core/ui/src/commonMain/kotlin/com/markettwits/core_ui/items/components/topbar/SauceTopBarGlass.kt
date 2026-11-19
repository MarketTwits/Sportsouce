package com.markettwits.core_ui.items.components.topbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.*
import kotlinx.coroutines.flow.collectLatest

/** Each screen owns its backdrop. Modal windows and neighbouring navigation stacks cannot leak in. */
@Stable
class SauceTopBarState internal constructor(val haze: HazeState) {
    private val areas = mutableStateMapOf<Any, TopBarArea>()
    internal val backdropReveal = Animatable(0f)

    internal val hasRecordedBackdrop by derivedStateOf {
        haze.areas.any { area ->
            // Haze publishes the layer just before recording it. Its measured area is
            // observable, while the graphics layer's own size does not trigger a snapshot.
            area.positionOnScreen.isSpecified && area.size.isSpecified && area.size.minDimension > 0f &&
                area.contentLayer?.isReleased == false
        }
    }

    internal fun liveGlassAlpha(requestedAlpha: Float): Float =
        if (hasRecordedBackdrop) requestedAlpha * backdropReveal.value else 0f

    /** Toolbar and its pinned sections form one capture region, even while the toolbar collapses. */
    internal val bounds: TopBarBounds?
        get() {
            val anchor = areas.values.firstOrNull { it.primary } ?: areas.values.firstOrNull() ?: return null
            val siblings = areas.values.filter {
                it.bounds.root === anchor.bounds.root && it.bounds.rect.height > 0 && it.bounds.rect.width > 0
            }
            if (siblings.isEmpty()) return anchor.bounds
            return TopBarBounds(anchor.bounds.root, Rect(
                siblings.minOf { it.bounds.rect.left }, siblings.minOf { it.bounds.rect.top },
                siblings.maxOf { it.bounds.rect.right }, siblings.maxOf { it.bounds.rect.bottom },
            ))
        }

    internal fun update(key: Any, coordinates: LayoutCoordinates, primary: Boolean) {
        val p = coordinates.positionInRoot()
        areas[key] = TopBarArea(TopBarBounds(coordinates.findRootCoordinates(),
            Rect(p.x, p.y, p.x + coordinates.size.width, p.y + coordinates.size.height)), primary)
    }

    internal fun remove(key: Any) { areas.remove(key) }
}

private data class TopBarArea(val bounds: TopBarBounds, val primary: Boolean)

internal data class TopBarBounds(val root: LayoutCoordinates, val rect: Rect)
val LocalSauceTopBar = staticCompositionLocalOf<SauceTopBarState?> { null }
internal val LocalInsideGlassTopBar = staticCompositionLocalOf { false }

@Composable
fun rememberSauceTopBarState(): SauceTopBarState {
    val haze = rememberHazeState()
    val state = remember(haze) { SauceTopBarState(haze) }
    LaunchedEffect(state) {
        snapshotFlow { state.hasRecordedBackdrop }.collectLatest { ready ->
            state.backdropReveal.snapTo(0f)
            if (ready) state.backdropReveal.animateTo(1f, tween(160))
        }
    }
    return state
}

/** Only the container becomes transparent; fields, chips, text and icons retain their own styling. */
@Composable
fun sauceTopBarContainerColor(): Color =
    if (LocalInsideGlassTopBar.current) Color.Transparent else SauceTheme.colors.surface

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun Modifier.sauceTopBarGlass(
    state: SauceTopBarState,
    primary: Boolean = true,
    containerColor: Color = Color.Unspecified,
    effectAlpha: Float = 1f,
): Modifier {
    val key = remember(state) { Any() }
    DisposableEffect(state, key) { onDispose { state.remove(key) } }
    val surface = containerColor.takeOrElse { SauceTheme.colors.surface }
    val dark = LocalDarkOrLightTheme.current
    val style = HazeStyle(
        backgroundColor = surface,
        tint = HazeTint(surface.copy(alpha = if (dark) 0.72f else 0.70f)),
        blurRadius = 20.dp,
        noiseFactor = 0.01f,
        fallbackTint = HazeTint(surface.copy(alpha = 0.96f)),
    )
    val materialAlpha = effectAlpha.coerceIn(0f, 1f)
    return onGloballyPositioned { state.update(key, it, primary) }
        .clipToBounds()
        .drawBehind {
            val liveAlpha = state.liveGlassAlpha(materialAlpha)
            // Compensate for source-over so partially revealed glass cannot expose a
            // frame of raw content while the new scroll viewport registers its layer.
            if (liveAlpha < materialAlpha) {
                val backingAlpha = (materialAlpha - liveAlpha) / (1f - liveAlpha)
                drawRect(surface.copy(alpha = surface.alpha * backingAlpha))
            }
        }
        .hazeEffect(state.haze, style) {
            inputScale = HazeInputScale.Auto
            alpha = state.liveGlassAlpha(materialAlpha)
        }
}

/** Non-scrolling full-bleed content, e.g. the photo editor, can be a backdrop without moving controls. */
@Composable
fun SauceTopBarBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val state = LocalSauceTopBar.current
    Box(modifier.then(if (state != null) Modifier.hazeSource(state.haze) else Modifier), content = content)
}

package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.*

/** Each screen owns its backdrop. Modal windows and neighbouring navigation stacks cannot leak in. */
@Stable
class SauceTopBarState internal constructor(val haze: HazeState) {
    private val areas = mutableStateMapOf<Any, TopBarArea>()

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
    return remember(haze) { SauceTopBarState(haze) }
}

/** Only the container becomes transparent; fields, chips, text and icons retain their own styling. */
@Composable
fun sauceTopBarContainerColor(): Color =
    if (LocalInsideGlassTopBar.current) Color.Transparent else SauceTheme.colors.surface

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun Modifier.sauceTopBarGlass(state: SauceTopBarState, primary: Boolean = true): Modifier {
    val key = remember(state) { Any() }
    DisposableEffect(state, key) { onDispose { state.remove(key) } }
    val colors = SauceTheme.colors
    val dark = LocalDarkOrLightTheme.current
    val style = HazeStyle(
        backgroundColor = colors.surface,
        tint = HazeTint(colors.surface.copy(alpha = if (dark) 0.72f else 0.70f)),
        blurRadius = 20.dp,
        noiseFactor = 0.01f,
        fallbackTint = HazeTint(colors.surface.copy(alpha = 0.96f)),
    )
    return onGloballyPositioned { state.update(key, it, primary) }.clipToBounds().hazeEffect(state.haze, style) {
        inputScale = HazeInputScale.Auto
    }
}

/** Non-scrolling full-bleed content, e.g. the photo editor, can be a backdrop without moving controls. */
@Composable
fun SauceTopBarBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val state = LocalSauceTopBar.current
    Box(modifier.then(if (state != null) Modifier.hazeSource(state.haze) else Modifier), content = content)
}

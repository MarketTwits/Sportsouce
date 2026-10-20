package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar
import dev.chrisbanes.haze.hazeSource
import kotlin.math.roundToInt

private val LocalVerticalScrollOwner = modifierLocalOf { false }

/** Insets kept inside the outer scroll content, so its viewport can draw behind system bars. */
val LocalScrollContentBottomPadding = compositionLocalOf { 0.dp }

/** Only the outer vertical viewport owns the overlap; embedded scroll areas keep their size. */
@Composable
internal fun rememberNavigationScrollViewport(): NavigationScrollViewport {
    val host = LocalNavigationViewport.current
    val topHost = LocalSauceTopBar.current
    val safeBottom = LocalScrollContentBottomPadding.current
    if (host == null && topHost == null && safeBottom == 0.dp) return NavigationScrollViewport(Modifier, 0.dp, 0.dp)
    val density = LocalDensity.current
    var hasScrollParent by remember { mutableStateOf(false) }
    var bounds by remember { mutableStateOf<NavigationViewportBounds?>(null) }
    val root = host?.bounds
    val current = bounds
    val reachesNavigation = !hasScrollParent && root != null && current != null &&
        root.overlap > 0 && current.root === root.root &&
        current.rect.top < root.rect.bottom - root.overlap &&
        current.rect.bottom >= root.rect.bottom - root.overlap - 1f
    val extra = if (reachesNavigation) root.overlap else 0
    val padding = if (reachesNavigation) with(density) {
        (extra + (current.rect.bottom - (root.rect.bottom - root.overlap)).coerceAtLeast(0f)).toDp()
    } else 0.dp
    val top = topHost?.bounds
    val reachesTop = !hasScrollParent && top != null && current != null &&
        current.root === top.root && top.rect.height > 0 &&
        current.rect.bottom > top.rect.bottom && current.rect.top >= top.rect.top - 1f &&
        current.rect.left < top.rect.right && current.rect.right > top.rect.left
    val topExtra = if (reachesTop) (current.rect.top - top.rect.top).roundToInt().coerceAtLeast(0) else 0
    val topPadding = with(density) { topExtra.toDp() }
    val modifier = Modifier
        .modifierLocalConsumer { hasScrollParent = LocalVerticalScrollOwner.current }
        .modifierLocalProvider(LocalVerticalScrollOwner) { true }
        .onGloballyPositioned { coordinates ->
            val position = coordinates.positionInRoot()
            bounds = NavigationViewportBounds(coordinates.findRootCoordinates(),
                Rect(position.x, position.y, position.x + coordinates.size.width, position.y + coordinates.size.height), 0)
        }
        // Keep raw content below pinned siblings; the retained source alone extends under glass.
        .then(if (reachesTop) Modifier.clipToBounds() else Modifier)
        .layout { measurable, constraints ->
            val growTop = if (constraints.hasBoundedHeight) topExtra else 0
            val grow = if (constraints.hasBoundedHeight) extra + growTop else 0
            val child = measurable.measure(constraints.copy(
                minHeight = constraints.minHeight + grow,
                maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight + grow else constraints.maxHeight,
            ))
            // The parent slot stays safe for pinned siblings; only scrolling content draws under chrome.
            layout(child.width, (child.height - grow).coerceAtLeast(0)) { child.placeRelative(0, -growTop) }
        }
    val sourceKey = remember { Any() }
    // Capture before ancestor Surface/Scaffold clipping. Enlarging a descendant alone cannot
    // make it visible through an ancestor's clip, but root glass can sample this retained layer.
    return NavigationScrollViewport(
        modifier
            .then(if (reachesNavigation) host.scrollSource(sourceKey) else Modifier)
            .then(if (reachesTop) Modifier.hazeSource(topHost.haze, key = sourceKey) else Modifier),
        padding + if (!hasScrollParent) safeBottom else 0.dp, topPadding,
    )
}

internal class NavigationScrollViewport(val modifier: Modifier, val bottomPadding: Dp, val topPadding: Dp)

@Composable
internal fun PaddingValues.withNavigationPadding(extra: Dp, topExtra: Dp = 0.dp): PaddingValues {
    if (extra == 0.dp && topExtra == 0.dp) return this
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(direction), top = calculateTopPadding() + topExtra,
        end = calculateEndPadding(direction), bottom = calculateBottomPadding() + extra,
    )
}

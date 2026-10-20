package com.markettwits.core_ui.items.components.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.positionInRoot

/** Root chrome geometry. Descendants never need to know which navigation stack owns them. */
@Stable
class NavigationViewport(
    internal val scrollSource: (key: Any) -> Modifier = { Modifier },
) {
    internal var bounds by mutableStateOf<NavigationViewportBounds?>(null)
        private set

    fun update(coordinates: LayoutCoordinates, bottomOverlapPx: Int) {
        val position = coordinates.positionInRoot()
        bounds = NavigationViewportBounds(
            root = coordinates.findRootCoordinates(),
            rect = Rect(position.x, position.y, position.x + coordinates.size.width, position.y + coordinates.size.height),
            overlap = bottomOverlapPx.coerceAtLeast(0),
        )
    }
}

internal data class NavigationViewportBounds(val root: LayoutCoordinates, val rect: Rect, val overlap: Int)

/** Null outside root navigation and inside modal content. */
val LocalNavigationViewport = staticCompositionLocalOf<NavigationViewport?> { null }

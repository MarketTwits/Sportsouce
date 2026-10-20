package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll as foundationVerticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/** Standard verticalScroll behavior, with root-navigation overlap managed by the design system. */
fun Modifier.sauceVerticalScroll(
    state: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
): Modifier = composed {
    val viewport = rememberNavigationScrollViewport()
    viewport.modifier.foundationVerticalScroll(state, enabled, flingBehavior, reverseScrolling)
        .padding(top = viewport.topPadding, bottom = viewport.bottomPadding)
}

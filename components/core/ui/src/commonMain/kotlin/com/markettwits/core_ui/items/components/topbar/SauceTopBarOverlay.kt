package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/** Shared glass for screens whose toolbar floats over a full-bleed cover. */
@Composable
fun SauceTopBarOverlayHost(content: @Composable () -> Unit) {
    val glass = rememberSauceTopBarState()
    CompositionLocalProvider(LocalSauceTopBar provides glass, content = content)
}

/** The material fades independently of sharp, interactive toolbar controls. */
@Composable
fun SauceTopBarOverlay(
    modifier: Modifier = Modifier,
    primary: Boolean = true,
    glassAlpha: () -> Float = { 1f },
    content: @Composable BoxScope.() -> Unit,
) {
    val glass = LocalSauceTopBar.current
    Box(modifier) {
        if (glass != null) Box(Modifier.matchParentSize().graphicsLayer { alpha = glassAlpha().coerceIn(0f, 1f) }
            .sauceTopBarGlass(glass, primary))
        CompositionLocalProvider(LocalInsideGlassTopBar provides true) { content() }
    }
}

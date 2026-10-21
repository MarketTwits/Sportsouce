package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.*

private val LocalPhotoViewerBackdrop = staticCompositionLocalOf<HazeState?> { null }

/** Capture the album, including its toolbar, without capturing the viewer's photo or controls. */
@Composable
fun SaucePhotoViewerBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val state = rememberHazeState()
    CompositionLocalProvider(LocalPhotoViewerBackdrop provides state) {
        Box(modifier.hazeSource(state), content = content)
    }
}

@Composable
internal fun photoViewerBackdropState(): HazeState? = LocalPhotoViewerBackdrop.current

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun PhotoViewerBackdropLayer(state: HazeState?, progress: Float, modifier: Modifier = Modifier) {
    val background = SauceTheme.colors.background
    val material = if (state != null) {
        Modifier.hazeEffect(state, HazeStyle(
            backgroundColor = background,
            tint = HazeTint(background.copy(alpha = .84f)),
            blurRadius = 36.dp,
            noiseFactor = .01f,
            fallbackTint = HazeTint(background.copy(alpha = .98f)),
        )) { inputScale = HazeInputScale.Auto }
    } else Modifier.background(background)
    // Animate the entire material so the first frame still shows the untouched album.
    Box(modifier.graphicsLayer { alpha = progress.coerceIn(0f, 1f) }.clipToBounds().then(material))
}

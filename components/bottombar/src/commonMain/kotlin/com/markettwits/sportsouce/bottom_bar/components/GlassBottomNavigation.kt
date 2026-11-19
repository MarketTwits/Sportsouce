package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

/** Backdrop glass is restricted to root navigation; the foreground stays unblurred. */
@Composable
@OptIn(ExperimentalHazeApi::class)
internal fun GlassBottomNavigation(
    hazeState: HazeState,
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    showLabels: Boolean,
    onSelect: (BottomBarConfiguration) -> Unit,
    profilePhotoUrl: String? = null,
) {
    if (items.isEmpty()) return

    val colors = SauceTheme.colors
    val dark = LocalDarkOrLightTheme.current
    val shape = SauceShapes.button
    val navigationInsets = WindowInsets.navigationBars
    val style = HazeStyle(
        backgroundColor = colors.background,
        tint = HazeTint(Brush.verticalGradient(listOf(
            colors.surfaceRaised.copy(alpha = if (dark) 0.64f else 0.68f),
            colors.surface.copy(alpha = if (dark) 0.80f else 0.83f),
        ))),
        blurRadius = 22.dp,
        noiseFactor = 0.018f,
        // Do not opt older Android into the experimental CPU/RenderScript blur path.
        fallbackTint = HazeTint(colors.surface.copy(alpha = 0.96f)),
    )
    Box(
        modifier = Modifier.fillMaxWidth()
            .drawNavigationScrim(navigationInsets, colors.background)
            .windowInsetsPadding(navigationInsets)
            .padding(horizontal = SauceSpacing.large)
            .padding(top = SauceSpacing.small, bottom = SauceSpacing.medium),
        contentAlignment = Alignment.Center,
    ) {
        BottomNavigation(
            items = items,
            selectedTab = selectedTab,
            showLabels = showLabels,
            onSelect = onSelect,
            profilePhotoUrl = profilePhotoUrl,
            modifier = Modifier.widthIn(max = 328.dp).fillMaxWidth()
                .shadow(10.dp, shape, clip = false)
                .clip(shape)
                .hazeEffect(hazeState, style) {
                    inputScale = HazeInputScale.Auto
                }
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(
                        colors.textPrimary.copy(alpha = if (dark) 0.24f else 0.13f),
                        colors.border.copy(alpha = if (dark) 0.54f else 0.82f),
                    )),
                    shape = shape,
                ),
        )
    }
}

private fun Modifier.drawNavigationScrim(
    navigationInsets: WindowInsets,
    background: Color,
): Modifier = drawBehind {
    val capsuleTop = SauceSpacing.small.toPx()
    val capsuleBottom = size.height - navigationInsets.getBottom(this) - SauceSpacing.medium.toPx()
    val fadeStart = (capsuleTop + capsuleBottom) / 2f
    if (fadeStart < size.height) {
        drawRect(
            Brush.verticalGradient(
                0f to background.copy(alpha = 0f),
                0.18f to background.copy(alpha = 0.32f),
                0.44f to background.copy(alpha = 0.77f),
                0.75f to background.copy(alpha = 0.97f),
                1f to background,
                startY = fadeStart,
                endY = size.height,
            ),
        )
    }
}

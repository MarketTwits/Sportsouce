package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
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
) {
    val colors = SauceTheme.colors
    val dark = LocalDarkOrLightTheme.current
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val style = HazeStyle(
        backgroundColor = colors.background,
        // Stronger tint behind labels keeps contrast even above a bright photo in dark mode.
        tint = HazeTint(Brush.verticalGradient(listOf(
            colors.surface.copy(alpha = if (dark) 0.80f else 0.84f),
            colors.surface.copy(alpha = 0.94f),
        ))),
        blurRadius = 28.dp,
        noiseFactor = 0.015f,
        // Do not opt older Android into the experimental CPU/RenderScript blur path.
        fallbackTint = HazeTint(colors.surface.copy(alpha = 0.97f)),
    )
    BottomNavigation(
        items, selectedTab, showLabels, onSelect,
        containerColor = Color.Transparent,
        glass = true,
        modifier = Modifier.clip(shape).hazeEffect(hazeState, style) {
            inputScale = HazeInputScale.Auto
        }.drawWithCache {
            val edge = Brush.horizontalGradient(listOf(
                Color.Transparent,
                colors.surfaceRaised.copy(alpha = if (dark) 0.55f else 0.9f),
                Color.Transparent,
            ))
            onDrawWithContent {
                drawContent()
                // A quiet specular edge, rather than an opaque divider above the material.
                drawLine(
                    brush = edge,
                    start = Offset(24.dp.toPx(), 0.5.dp.toPx()),
                    end = Offset(size.width - 24.dp.toPx(), 0.5.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                )
            }
        },
    )
}

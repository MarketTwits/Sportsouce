package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.takeOrElse
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

private val ActionSize = 40.dp

@ConsistentCopyVisibility
data class SauceFloatingActionGlass internal constructor(
    internal val backdrop: HazeState,
    internal val amount: Float,
    internal val tint: Color,
)

@Composable
fun sauceFloatingActionTopPadding() = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 10.dp

/** The material changes as the bottom edge of the backdrop moves across the visible button. */
@Composable
fun rememberSauceFloatingActionGlass(
    backdrop: HazeState,
    tint: Color = SauceTheme.colors.surfaceRaised.copy(alpha = .32f),
    backdropBottom: () -> Int,
): SauceFloatingActionGlass? {
    val topPadding = sauceFloatingActionTopPadding()
    val touchSize = LocalMinimumInteractiveComponentSize.current.takeOrElse { ActionSize }
    val touchPadding = (touchSize - ActionSize).coerceAtLeast(0.dp) / 2
    val density = LocalDensity.current
    val glass by remember(backdrop, tint, density, topPadding, touchPadding, backdropBottom) {
        val actionTop = with(density) { (topPadding + touchPadding).roundToPx() }
        val actionHeight = with(density) { ActionSize.roundToPx() }
        derivedStateOf {
            val amount = ((backdropBottom() - actionTop).toFloat() / actionHeight).coerceIn(0f, 1f)
            if (amount > 0f) SauceFloatingActionGlass(backdrop, amount, tint) else null
        }
    }
    return glass
}

@OptIn(ExperimentalHazeApi::class)
@Composable
fun SauceFloatingActionButton(
    modifier: Modifier = Modifier,
    glass: SauceFloatingActionGlass? = null,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colors = SauceTheme.colors
    val shape = FloatingActionButtonDefaults.smallShape
    val glassModifier = if (glass != null) {
        Modifier.shadow(6.dp * (1f - glass.amount), shape, clip = false)
            .clip(shape)
            .hazeEffect(
                state = glass.backdrop,
                style = HazeStyle(
                    backgroundColor = colors.background,
                    tint = HazeTint(glass.tint),
                    blurRadius = 20.dp,
                    noiseFactor = .01f,
                    fallbackTint = HazeTint(glass.tint.copy(alpha = glass.tint.alpha.coerceAtLeast(.58f))),
                ),
            ) { inputScale = HazeInputScale.Auto }
    } else {
        Modifier
    }
    SmallFloatingActionButton(
        modifier = modifier.minimumInteractiveComponentSize().size(ActionSize).then(glassModifier),
        shape = shape,
        containerColor = colors.surfaceRaised.copy(alpha = 1f - (glass?.amount ?: 0f)),
        contentColor = colors.textPrimary,
        elevation = if (glass != null) {
            FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        } else {
            FloatingActionButtonDefaults.elevation()
        },
        onClick = onClick,
        content = content,
    )
}

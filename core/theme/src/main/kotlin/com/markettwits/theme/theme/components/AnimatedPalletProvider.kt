package com.markettwits.theme.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private const val ANIMATION_DURATION_MS = 750
private val animationSpec: AnimationSpec<Color> = tween(ANIMATION_DURATION_MS)

@Composable
private fun animateColor(
    targetValue: Color
) = animateColorAsState(
    targetValue = targetValue,
    animationSpec = animationSpec,
    label = "$targetValue"
).value

@Composable
internal fun ColorScheme.toAnimatePallet() = lightColorScheme(
    primary = animateColor(this.primary),
    onPrimary = animateColor(this.onPrimary),
    primaryContainer = animateColor(this.primaryContainer),
    onPrimaryContainer = animateColor(this.onPrimaryContainer),
    secondary = animateColor(this.secondary),
    onSecondary = animateColor(this.onSecondary),
    tertiary = animateColor(this.tertiary),
    onTertiary = animateColor(this.onTertiary),
    tertiaryContainer = animateColor(this.tertiaryContainer),
    onTertiaryContainer = animateColor(this.onTertiaryContainer),
    onErrorContainer = animateColor(this.onErrorContainer),
    outline = animateColor(this.outline),
    outlineVariant = animateColor(this.outlineVariant),
    onBackground = animateColor(this.onBackground),
    background = animateColor(this.background)
)


package com.markettwits.core_ui.items.image

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.takeOrElse
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceTheme

/** Softens an image color with the active theme, keeping text on the backdrop readable. */
@Composable
fun rememberImageBackdropTint(imageColor: Color): State<Color> {
    val background = SauceTheme.colors.background
    return animateColorAsState(
        targetValue = lerp(background, imageColor.takeOrElse { background }, if (LocalDarkOrLightTheme.current) .28f else .19f),
        animationSpec = tween(460),
        label = "Image backdrop tint",
    )
}

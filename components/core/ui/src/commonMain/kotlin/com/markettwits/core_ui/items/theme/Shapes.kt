package com.markettwits.core_ui.items.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** The same scale is installed in MaterialTheme and used by legacy shape aliases. */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(10.dp),
    medium = SauceShapes.field,
    large = SauceShapes.card,
    extraLarge = RoundedCornerShape(26.dp)
)

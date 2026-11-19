package com.markettwits.core_ui.items.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object SauceSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val extraLarge = 24.dp
    val section = 32.dp
}

object SauceSizes {
    val touchTarget = 48.dp
    val fieldMinHeight = 56.dp
    val icon = 24.dp
}

object SauceShapes {
    val badge = RoundedCornerShape(10.dp)
    val field = RoundedCornerShape(14.dp)
    val card = RoundedCornerShape(22.dp)
    val button = CircleShape
    val sheet = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
}

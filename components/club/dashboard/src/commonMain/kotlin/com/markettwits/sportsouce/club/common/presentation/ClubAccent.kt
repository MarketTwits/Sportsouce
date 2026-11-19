package com.markettwits.sportsouce.club.common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceStatusColors
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor

@Immutable
internal data class ClubAccentColors(val content: Color, val container: Color, val stripeEnd: Color) {
    val selectionColors get() = SauceStatusColors(content, container)
}

/** Website plan colors; softer surfaces and brighter foregrounds keep the same meaning at night. */
internal fun SubscriptionColor.clubColors(dark: Boolean): ClubAccentColors = when (this) {
    SubscriptionColor.GREEN -> ClubAccentColors(Color(if (dark) 0xFF79DAB7 else 0xFF137A52), Color(if (dark) 0xFF173D34 else 0xFFE8F6EF), Color(0xFF5ECFB1))
    SubscriptionColor.BLUE -> ClubAccentColors(Color(if (dark) 0xFFA8BFFF else 0xFF3159DF), Color(if (dark) 0xFF24375A else 0xFFEDF3FE), Color(0xFF6DB6FD))
    SubscriptionColor.PURPLE -> ClubAccentColors(Color(if (dark) 0xFFCFB4FF else 0xFF7250B8), Color(if (dark) 0xFF352B4B else 0xFFF3EEFC), Color(0xFFAA88E0))
    SubscriptionColor.ORANGE -> ClubAccentColors(Color(if (dark) 0xFFF4BA82 else 0xFFA65815), Color(if (dark) 0xFF44301F else 0xFFFFF1DF), Color(0xFFF0B874))
}

@Composable
internal fun SubscriptionColor.clubColors() = clubColors(LocalDarkOrLightTheme.current)

/** Workouts have no color field; match their direction to the website's subscription palette. */
internal fun clubDirectionAccent(direction: String): SubscriptionColor {
    val name = direction.lowercase()
    return when {
        "функц" in name || "силов" in name -> SubscriptionColor.ORANGE
        "плав" in name -> SubscriptionColor.PURPLE
        "лыж" in name -> SubscriptionColor.BLUE
        "бег" in name -> SubscriptionColor.GREEN
        else -> SubscriptionColor.BLUE
    }
}

@Composable
internal fun ClubAccentStripe(accent: SubscriptionColor) {
    val colors = accent.clubColors()
    Box(Modifier.fillMaxWidth().height(4.dp).background(Brush.horizontalGradient(listOf(colors.content, colors.stripeEnd))))
}

@Composable
internal fun ClubAccentLabel(text: String, accent: SubscriptionColor, modifier: Modifier = Modifier) {
    val colors = accent.clubColors()
    Surface(modifier, color = colors.container, shape = SauceShapes.badge) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, color = colors.content)
    }
}

package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.absoluteValue

@Composable
fun resolveNewsCategoryColor(categoryName: String): Color {
    return when (val normalized = categoryName.trim().lowercase()) {
        "все", "новости", "новости сайта" -> Color(0xFF00C18C)
        "информация к стартам" -> Color(0xFF2979FF)
        "старты по сибири" -> Color(0xFFFF5A1F)
        "анонсы стартов" -> Color(0xFFB239FF)
        else -> expressiveColorFromName(normalized)
    }
}

fun categoryBadgeTextColor(background: Color): Color {
    return if (background.luminance() > 0.5f) Color.Black else Color.White
}

private fun expressiveColorFromName(name: String): Color {
    val expressivePalette = listOf(
        Color(0xFF1E6BFF),
        Color(0xFFFD5E53),
        Color(0xFF9F4BFF),
        Color(0xFFFF7A00),
        Color(0xFF00B8D9),
        Color(0xFFEF476F),
        Color(0xFFFFB703),
        Color(0xFF5A67FF),
        Color(0xFFFF4D8D),
        Color(0xFF2C7BE5),
        Color(0xFFFF914D),
        Color(0xFF7E57FF),
    )
    val seed = (name.hashCode().absoluteValue * 1103515245L + 12345L).absoluteValue
    val base = expressivePalette[(seed % expressivePalette.size).toInt()]
    return adjustAwayFromGreen(base)
}

private fun adjustAwayFromGreen(color: Color): Color {
    // Keep "Все" uniquely green: unknown colors that become too green are shifted.
    return if (color.green > color.red * 1.25f && color.green > color.blue * 1.25f) {
        Color(
            red = (color.red + 0.22f).coerceIn(0f, 1f),
            green = (color.green - 0.16f).coerceIn(0f, 1f),
            blue = (color.blue + 0.14f).coerceIn(0f, 1f),
            alpha = 1f
        )
    } else {
        color
    }
}

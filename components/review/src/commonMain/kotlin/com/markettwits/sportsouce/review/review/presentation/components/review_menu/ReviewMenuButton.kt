package com.markettwits.sportsouce.review.review.presentation.components.review_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme

private val ReviewMenuDarkSurface = Color(0xFF171A20)

@Composable
fun ReviewMenuButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    accentColor: Color,
    backgroundColor: Color,
    onClick : () -> Unit,
) {
    val isDark = LocalDarkOrLightTheme.current
    val shape = RoundedCornerShape(28.dp)
    val darkAdaptedAccent = lerp(start = accentColor, stop = Color.White, fraction = 0.62f)
    val iconColor = if (isDark) darkAdaptedAccent else accentColor
    val textColor = if (isDark) darkAdaptedAccent else accentColor
    val containerColor = if (isDark) {
        accentColor.copy(alpha = 0.26f).compositeOver(ReviewMenuDarkSurface)
    } else {
        backgroundColor
    }

    Row(
        modifier = modifier
            .heightIn(min = 58.dp)
            .clip(shape)
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            color = textColor,
            maxLines = 1,
            fontSize = 15.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.bold(),
            modifier = Modifier.weight(1f)
        )
    }
}

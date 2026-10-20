package com.markettwits.core_ui.items.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*

/** A navigation/document row. Its whole surface is one accessible action; text can grow. */
@Composable
fun SauceActionRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    count: String? = null,
    contentPadding: PaddingValues = PaddingValues(SauceSpacing.small),
) {
    val colors = SauceTheme.colors
    Surface(modifier = modifier.fillMaxWidth().heightIn(min = SauceSizes.touchTarget), onClick = onClick,
        shape = SauceShapes.field, color = colors.surface, contentColor = colors.textPrimary) {
        BoxWithConstraints(Modifier.padding(contentPadding), contentAlignment = Alignment.Center) {
            val narrow = maxWidth < 280.dp * LocalDensity.current.fontScale
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                if (icon != null) Icon(icon, null, Modifier.size(SauceSizes.icon), tint = colors.textSecondary)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                    Text(title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall)
                    if (!subtitle.isNullOrBlank()) Text(subtitle, Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                    if (narrow && !count.isNullOrBlank()) ActionRowCount(count)
                }
                if (!narrow && !count.isNullOrBlank()) ActionRowCount(count)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                    Modifier.size(SauceSizes.icon), tint = colors.textMuted)
            }
        }
    }
}

@Composable
private fun ActionRowCount(value: String) {
    val style = MaterialTheme.typography.labelMedium
    val measured = rememberTextMeasurer().measure(value, style = style)
    val width = with(LocalDensity.current) { measured.size.width.toDp() } + 1.dp
    Surface(color = SauceTheme.colors.surfaceSubtle, contentColor = SauceTheme.colors.textSecondary,
        shape = SauceShapes.badge) {
        Text(value, Modifier.padding(horizontal = SauceSpacing.small, vertical = SauceSpacing.extraSmall).width(width), style = style)
    }
}

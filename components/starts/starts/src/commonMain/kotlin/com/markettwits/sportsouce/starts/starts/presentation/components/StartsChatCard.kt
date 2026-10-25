package com.markettwits.sportsouce.starts.starts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun StartsChatCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = SauceTheme.colors
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(SauceShapes.card)
            .background(Brush.horizontalGradient(listOf(colors.actionSoft, colors.success.container)))
            .clickable(role = Role.Button, onClickLabel = "Перейти в чат", onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(IconTelegram, contentDescription = null,
            modifier = Modifier.size(40.dp), tint = colors.action)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Стартуем вместе", style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary)
            Text("Присоединяйтесь к Telegram-чату: новости стартов и общение с участниками.",
                style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
        }
        Box(
            modifier = Modifier.size(32.dp).background(colors.surface, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
                modifier = Modifier.size(18.dp), tint = colors.action)
        }
    }
}

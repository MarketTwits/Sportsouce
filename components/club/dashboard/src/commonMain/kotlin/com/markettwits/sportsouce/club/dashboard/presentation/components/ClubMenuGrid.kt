package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.club.common.presentation.clubColors
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ClubMenuGrid(onPlanClick: () -> Unit, onClubBonusesClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ClubDiscoveryCard("Возможности клуба", "Больше, чем тренировки", Icons.Default.StarOutline, SubscriptionColor.PURPLE, onClubBonusesClick)
        ClubDiscoveryCard("Как начать", "От знакомства до вашей цели", Icons.Default.Flag, SubscriptionColor.ORANGE, onPlanClick)
    }
}

@Composable
private fun ClubDiscoveryCard(title: String, subtitle: String, icon: ImageVector, accent: SubscriptionColor, onClick: () -> Unit) {
    val colors = accent.clubColors()
    Surface(onClick = onClick, shape = SauceShapes.card, color = colors.container) {
        Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(icon, null, Modifier.size(28.dp), tint = colors.content)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(20.dp), tint = colors.content)
        }
    }
}

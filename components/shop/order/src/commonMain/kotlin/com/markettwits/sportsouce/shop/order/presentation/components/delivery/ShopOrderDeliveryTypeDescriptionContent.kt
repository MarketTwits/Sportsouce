package com.markettwits.sportsouce.shop.order.presentation.components.delivery

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopDeliveryTypeDescriptionContent(modifier: Modifier = Modifier, deliveryTitle: String, deliveryPlace: String) {
    OnBackgroundCard(modifier) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.LocationOn, null, Modifier.size(24.dp), tint = SauceTheme.colors.textMuted)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(deliveryTitle, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                Text(deliveryPlace, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary)
            }
        }
    }
}

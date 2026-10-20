package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun ShopOrderInfoRow(modifier: Modifier = Modifier, title: String, value: String) {
    FlowRow(modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary)
    }
}

package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopUserOrdersWithoutItemsContent(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(vertical = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.ShoppingBag, null, Modifier.size(48.dp), tint = SauceTheme.colors.textMuted)
        Text("У вас пока нет заказов", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
        Text("Здесь появятся ваши покупки в магазине", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
    }
}

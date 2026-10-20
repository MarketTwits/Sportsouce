package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopOrderItemRow(modifier: Modifier = Modifier, imageUrl: String?, title: String, count: String, totalPrice: String) {
    Row(modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ShopOrderImage(imageUrl = imageUrl)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary,
                maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text("$count × $totalPrice ₽", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        }
    }
}

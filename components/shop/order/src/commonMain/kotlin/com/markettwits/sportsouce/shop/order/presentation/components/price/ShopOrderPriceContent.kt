package com.markettwits.sportsouce.shop.order.presentation.components.price

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.SaucePrice
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderPriceContent(modifier: Modifier = Modifier, itemsCount: String, discount: String, totalCost: String) {
    if (totalCost != "0") ShopBasicSectorContent(modifier, "Ваш заказ") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Товаров: $itemsCount", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            if (discount.isNotBlank() && discount != "0") Text("Скидка: $discount ₽",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.action)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Итого", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                SaucePrice(totalCost, prominent = true)
            }
        }
    }
}

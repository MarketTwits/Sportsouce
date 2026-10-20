package com.markettwits.sportsouce.shop.order.presentation.components.compositions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.SauceProductImage
import com.markettwits.core_ui.items.components.commerce.SaucePrice
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.shop.cart.domain.calculateTotalCost
import com.markettwits.sportsouce.shop.domain.mapper.formatPriceWithSpaces
import com.markettwits.sportsouce.shop.order.domain.model.ShopItemOrderResult
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderComposition(modifier: Modifier = Modifier, shopItems: List<ShopItemOrderResult>) {
    ShopBasicSectorContent(modifier, "Состав заказа") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            shopItems.forEach { result ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceProductImage(result.item.item.visual.imageUrl.firstOrNull(), Modifier.size(60.dp, 80.dp).clip(SauceShapes.badge))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(result.item.item.visual.displayName, style = MaterialTheme.typography.bodyMedium,
                            color = SauceTheme.colors.textPrimary, maxLines = 3, overflow = TextOverflow.Ellipsis)
                        Text("Количество: ${result.item.count}", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                        if (result.isAvailable) SaucePrice(formatPriceWithSpaces(result.item.calculateTotalCost()))
                        else result.message?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.danger.content) }
                    }
                }
            }
        }
    }
}

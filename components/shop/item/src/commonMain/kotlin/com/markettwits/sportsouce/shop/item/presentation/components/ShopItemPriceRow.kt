package com.markettwits.sportsouce.shop.item.presentation.components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.commerce.SaucePrice
import com.markettwits.sportsouce.shop.domain.model.ShopItem
@Composable
internal fun ShopItemPriceRow(modifier: Modifier = Modifier, price: ShopItem.Price) {
    SaucePrice(price.currentPrice, price.previousPrice, price.discount, modifier, prominent = true)
}

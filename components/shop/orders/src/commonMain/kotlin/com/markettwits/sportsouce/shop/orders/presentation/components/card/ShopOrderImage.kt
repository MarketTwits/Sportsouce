package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.SauceProductImage
import com.markettwits.core_ui.items.theme.SauceShapes

@Composable
internal fun ShopOrderImage(modifier: Modifier = Modifier, imageUrl: String?) {
    SauceProductImage(imageUrl, modifier.size(60.dp).clip(SauceShapes.badge))
}

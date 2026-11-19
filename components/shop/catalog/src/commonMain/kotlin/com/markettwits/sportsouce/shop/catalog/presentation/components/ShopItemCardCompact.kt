package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.*
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun ShopItemCardCompact(modifier: Modifier = Modifier, shopItem: ShopItem, onItemClick: (ShopItem) -> Unit,
    cardWidth: Dp = 140.dp, cardHeight: Dp = 230.dp, showBorder: Boolean = true,
    showImageBorder: Boolean = false, imageAspectRatio: Float? = null, imageBottomSpacing: Dp = 8.dp) {
    Surface(onClick = { onItemClick(shopItem) }, modifier = modifier.padding(6.dp).width(cardWidth).heightIn(min = cardHeight),
        shape = SauceShapes.field, color = SauceTheme.colors.surface,
        border = if (showBorder) BorderStroke(1.dp, SauceTheme.colors.border) else null) {
        Column {
            Surface(shape = SauceShapes.field, border = if (showImageBorder) BorderStroke(1.dp, SauceTheme.colors.border) else null) {
                SauceProductImage(shopItem.visual.imageUrl.firstOrNull(), Modifier.fillMaxWidth().aspectRatio(imageAspectRatio ?: 0.9f))
            }
            Column(Modifier.padding(horizontal = 8.dp).padding(top = imageBottomSpacing, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SaucePrice(shopItem.price.currentPrice, shopItem.price.previousPrice, shopItem.price.discount)
                Text(shopItem.visual.displayName, style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textPrimary, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

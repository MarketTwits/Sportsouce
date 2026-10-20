package com.markettwits.sportsouce.shop.item.presentation.components.similar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.commerce.*
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
internal fun ShopItemSimilarCard(modifier: Modifier = Modifier, shopItem: ShopItem, onItemClick: (ShopItem) -> Unit) {
    OnBackgroundCard(modifier, shape = SauceShapes.field, onClick = { onItemClick(shopItem) }) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceProductImage(shopItem.visual.imageUrl.firstOrNull(), Modifier.fillMaxWidth().aspectRatio(0.9f).clip(SauceShapes.badge))
            Text(shopItem.visual.displayName, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary,
                maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)
            SaucePrice(shopItem.price.currentPrice, shopItem.price.previousPrice, shopItem.price.discount)
        }
    }
}

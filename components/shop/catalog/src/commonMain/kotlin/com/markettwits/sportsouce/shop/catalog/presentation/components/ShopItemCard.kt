package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.commerce.SaucePrice
import com.markettwits.core_ui.items.components.commerce.SauceProductImage
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun ShopItemCard(modifier: Modifier = Modifier, shopItem: ShopItem, onItemClick: (ShopItem) -> Unit) {
    OnBackgroundCard(modifier, shape = SauceShapes.field, onClick = { onItemClick(shopItem) }) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val images = shopItem.visual.imageUrl
            val pager = rememberPagerState { images.size }
            if (images.isEmpty()) SauceProductImage(null, Modifier.fillMaxWidth().aspectRatio(0.9f).clip(SauceShapes.badge))
            else HorizontalPager(pager, Modifier.fillMaxWidth().aspectRatio(0.9f).clip(SauceShapes.badge)) { page ->
                SauceProductImage(images[page], Modifier.fillMaxSize())
            }
            if (images.size > 1) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                repeat(images.size) { page -> Box(Modifier.padding(2.dp).size(4.dp).clip(CircleShape)
                    .background(if (pager.currentPage == page) SauceTheme.colors.action else SauceTheme.colors.border)) }
            }
            Text(shopItem.visual.displayName, style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.textPrimary, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)
            SaucePrice(shopItem.price.currentPrice, shopItem.price.previousPrice, shopItem.price.discount)
        }
    }
}

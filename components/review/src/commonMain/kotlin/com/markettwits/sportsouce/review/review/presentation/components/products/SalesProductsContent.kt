package com.markettwits.sportsouce.review.review.presentation.components.products

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.sportsouce.shop.catalog.presentation.components.ShopItemCardCompact
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun SalesProductsContent(
    modifier: Modifier = Modifier,
    title: String,
    items: List<ShopItem>,
    onClickItem: (ShopItem) -> Unit,
    onClickShowMoreProducts: () -> Unit,
) {
    val isDarkTheme = LocalDarkOrLightTheme.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = modifier.padding(horizontal = 10.dp),
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
        TextButton(
            onClick = onClickShowMoreProducts
        ) {
            Text(
                text = "Показать ещё",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.medium(),
                fontSize = 14.sp
            )
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxItemsInColumn = when {
            maxWidth >= 1080.dp -> 3
            maxWidth >= 760.dp -> 2
            else -> 1
        }
        val cardWidth = (maxWidth * 0.16f).coerceIn(140.dp, 176.dp)
        val cardHeight = (cardWidth * 1.68f).coerceIn(236.dp, 296.dp)

        FlowColumn(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp),
            maxItemsInEachColumn = maxItemsInColumn,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items.forEach { item ->
                ShopItemCardCompact(
                    modifier = Modifier,
                    shopItem = item,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    showBorder = false,
                    showImageBorder = !isDarkTheme,
                    imageAspectRatio = 1f,
                    imageBottomSpacing = 8.dp,
                    onItemClick = {
                        onClickItem(item)
                    }
                )
            }
        }
    }
}

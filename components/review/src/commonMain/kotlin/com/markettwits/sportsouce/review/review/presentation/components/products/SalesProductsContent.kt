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
import com.markettwits.sportsouce.shop.catalog.presentation.components.ShopItemCardCompact
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun SalesProductsContent(
    modifier: Modifier = Modifier,
    items: List<ShopItem>,
    onClickItem: (ShopItem) -> Unit,
    onClickShowMoreProducts: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = modifier.padding(horizontal = 10.dp),
            text = "Акции",
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
        val cardWidth = (maxWidth * 0.145f).coerceIn(132.dp, 168.dp)
        val cardHeight = (cardWidth * 1.55f).coerceIn(208.dp, 262.dp)

        FlowColumn(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp),
            maxItemsInEachColumn = maxItemsInColumn,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items.forEach { item ->
                ShopItemCardCompact(
                    shopItem = item,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    showBorder = false,
                    onItemClick = {
                        onClickItem(item)
                    }
                )
            }
        }
    }
}

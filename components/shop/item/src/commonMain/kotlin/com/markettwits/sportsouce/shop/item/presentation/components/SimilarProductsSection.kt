package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.presentation.components.similar.ShopItemSimilarCard
import com.markettwits.sportsouce.shop.item.presentation.components.similar.ShopItemSimilarShimmerCard
import kotlin.math.min

fun LazyListScope.similarProductsItems(
    items: LazyPagingItems<ShopItem>,
    columns: Int,
    onClickItem: (ShopItem) -> Unit,
) {
    val isInitialLoading = items.loadState.refresh is LoadStateLoading && items.itemCount == 0

    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = "Похожие товары",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.bold()
        )
    }

    if (isInitialLoading) {
        items(2) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(columns) {
                    ShopItemSimilarShimmerCard(modifier = Modifier.weight(1f))
                }
            }
        }
    } else if (items.itemCount > 0) {
        items(
            count = (items.itemCount + columns - 1) / columns,
        ) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (columnIndex in 0 until columns) {
                    val itemIndex = rowIndex * columns + columnIndex
                    if (itemIndex < items.itemCount) {
                        items[itemIndex]?.let { shopItem ->
                            ShopItemSimilarCard(
                                modifier = Modifier.weight(1f),
                                shopItem = shopItem,
                                onItemClick = { onClickItem(it) }
                            )
                        } ?: Box(modifier = Modifier.weight(1f)) {
                            ShopItemSimilarShimmerCard()
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        if (items.loadState.append is LoadStateLoading) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(min(columns, 3)) {
                        ShopItemSimilarShimmerCard(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

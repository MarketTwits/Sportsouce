package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemCompactPane
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemLargePane
import kotlinx.coroutines.flow.Flow
import kotlin.math.min

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ShopItemProductScreen(
    item: ShopItem,
    options: List<ShopExtraOptions>,
    isLoading: Boolean,
    similarProducts: Flow<PagingData<ShopItem>>,
    onClickOption: (String) -> Unit,
    onClickGoBack: () -> Unit,
    onRefresh: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    onClickItem: (String) -> Unit,
    cartContent: @Composable (Modifier) -> Unit,
) {

    val windowSize = calculateWindowSizeClass()

    AdaptivePane {
        PullToRefreshScreen(
            isRefreshing = isLoading,
            onRefresh = onRefresh,
        ) {
            Scaffold(
                bottomBar = {
                    if (!windowSize.isLarge)
                        cartContent(Modifier.fillMaxWidth())
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                val similarItems = similarProducts.collectAsLazyPagingItems()
                val screenWidth = rememberScreenSizeInfo().wDP
                val maxColumns = 4
                val minColumnWidth = 180.dp
                val horizontalPadding = 16.dp
                val columns = remember(screenWidth) {
                    min(maxColumns, ((screenWidth - horizontalPadding) / minColumnWidth).toInt()).coerceAtLeast(1)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    item {
                        if (windowSize.isLarge) {
                            ShopItemLargePane(
                                item = item,
                                options = options,
                                cartContent = cartContent,
                                onClickOption = onClickOption
                            )
                        } else {
                            ShopItemCompactPane(
                                item = item,
                                options = options,
                                onClickOption = onClickOption
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    similarProductsItems(
                        items = similarItems,
                        columns = columns,
                        onClickItem = onClickItem
                    )

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                ShopItemActionRow(
                    modifier = Modifier.fillMaxWidth(),
                    onClickGoBack = onClickGoBack,
                    onClickShare = onClickShare
                )
            }
        }
    }
}
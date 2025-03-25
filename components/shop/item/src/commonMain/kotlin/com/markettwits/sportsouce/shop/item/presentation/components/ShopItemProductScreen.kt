package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemCompactPane
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemLargePane

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ShopItemProductScreen(
    item: ShopItem,
    options: List<ShopExtraOptions>,
    isLoading: Boolean,
    onClickOption: (String) -> Unit,
    onClickGoBack: () -> Unit,
    onRefresh: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    cartContent: @Composable (Modifier) -> Unit
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
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
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
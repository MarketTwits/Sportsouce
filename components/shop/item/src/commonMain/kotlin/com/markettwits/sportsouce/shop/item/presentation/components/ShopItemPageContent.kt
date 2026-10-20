package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import androidx.compose.ui.semantics.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore

@Composable
internal fun ShopItemPageContent(
    state: ShopItemPageStore.State,
    onClickOption: (String) -> Unit,
    onClickRetry: () -> Unit,
    onClickGoBack: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    onClickItem: (com.markettwits.sportsouce.shop.domain.model.ShopItem) -> Unit,
    cartContent: @Composable (Modifier) -> Unit,
) {
    if (state.isError && state.shopItem == null) {
        FailedScreen(
            message = state.message,
            onClickRetry = onClickRetry,
            onClickBack = onClickGoBack
        )
    }
    if (state.isLoading && state.shopItem == null) {
        Box(Modifier.fillMaxSize().semantics { contentDescription = "Загрузка товара"; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate }) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SauceSkeleton(Modifier.fillMaxWidth().height(360.dp))
                SauceSkeleton(Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(26.dp))
                SauceSkeleton(Modifier.padding(horizontal = 16.dp).width(120.dp).height(24.dp))
                SauceSkeleton(Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(80.dp))
            }
            BackFloatingActionButton(back = onClickGoBack)
        }
    }
    if (state.shopItem != null) {
        ShopItemProductScreen(
            item = state.shopItem,
            options = state.shopItemOptions,
            isLoading = state.isLoading,
            similarProducts = state.similarProducts,
            onClickOption = onClickOption,
            onClickGoBack = onClickGoBack,
            onRefresh = onClickRetry,
            onClickAddToFavorite = onClickAddToFavorite,
            onClickShare = onClickShare,
            onClickItem = onClickItem,
            cartContent = cartContent
        )
    }
}

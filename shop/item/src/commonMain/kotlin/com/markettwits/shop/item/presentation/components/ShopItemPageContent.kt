package com.markettwits.shop.item.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.shop.item.presentation.store.ShopItemPageStore

@Composable
internal fun ShopItemPageContent(
    state: ShopItemPageStore.State,
    onClickOption: (String) -> Unit,
    onClickRetry: () -> Unit,
    onClickGoBack: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    cartContent: @Composable (Modifier) -> Unit
) {
    if (state.isError) {
        FailedScreen(
            message = state.message,
            onClickRetry = onClickRetry,
            onClickBack = onClickGoBack
        )
    }
    if (state.isLoading && state.item == null) {
        LoadingFullScreen(onClickBack = onClickGoBack)
    }
    if (state.item != null) {
        ShopItemProductScreen(
            item = state.item,
            isLoading = state.isLoading,
            onClickOption = onClickOption,
            onClickGoBack = onClickGoBack,
            onRefresh = onClickRetry,
            onClickAddToFavorite = onClickAddToFavorite,
            onClickShare = onClickShare,
            content = cartContent
        )
    }
}

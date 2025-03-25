package com.markettwits.sportsouce.shop.orders.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder
import com.markettwits.sportsouce.shop.orders.presentation.components.states.ShopUserOrdersErrorContent
import com.markettwits.sportsouce.shop.orders.presentation.components.states.ShopUserOrdersItemsContent
import com.markettwits.sportsouce.shop.orders.presentation.components.states.ShopUserOrdersLoadingContent

@Composable
fun ShopUserOrdersContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isSuccess: Boolean,
    error: SauceError?,
    items: List<ShopUserOrder>,
    onClickGoBack: () -> Unit,
    onClickRetry: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarBase(title = "Мои заказы", goBack = onClickGoBack)
        }
    ) { paddingValues ->
        PullToRefreshScreen(
            isRefreshing = isLoading && items.isNotEmpty(),
            onRefresh = onClickRetry
        ) { innerModifier ->
            ShopUserOrdersItemsContent(
                modifier = innerModifier,
                paddingValues = paddingValues,
                isSuccess = isSuccess,
                items = items
            )
            ShopUserOrdersErrorContent(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                error = error,
                onClickRetry = onClickRetry
            )
            ShopUserOrdersLoadingContent(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                isLoading = isLoading,
                isListEmpty = items.isEmpty()
            )
        }
    }
}
package com.markettwits.shop.orders.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.shop.orders.domain.models.ShopUserOrder
import com.markettwits.shop.orders.presentation.components.states.ShopUserOrdersErrorContent
import com.markettwits.shop.orders.presentation.components.states.ShopUserOrdersLoadingContent
import com.markettwits.shop.orders.presentation.components.states.ShopUserOrdersWithItemsContent
import com.markettwits.shop.orders.presentation.components.states.ShopUserOrdersWithoutItemsContent

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
            if (isSuccess) {
                Column(
                    modifier = innerModifier
                        .padding(top = paddingValues.calculateTopPadding())
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (items.isNotEmpty()) {
                        ShopUserOrdersWithItemsContent(items = items)
                    } else {
                        ShopUserOrdersWithoutItemsContent()
                    }
                }
            }
        }
        ShopUserOrdersErrorContent(error = error, onClickRetry = onClickRetry)
        ShopUserOrdersLoadingContent(isLoading = isLoading, isListEmpty = items.isEmpty())
    }
}
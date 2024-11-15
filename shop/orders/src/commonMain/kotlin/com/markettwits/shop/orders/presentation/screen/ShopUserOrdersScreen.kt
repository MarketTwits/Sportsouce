package com.markettwits.shop.orders.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.shop.orders.presentation.components.ShopUserOrdersContent
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore

@Composable
fun ShopUserOrdersScreen(
    component: ShopUserOrdersComponent
) {
    val state by component.state.collectAsState()

    ShopUserOrdersContent(
        isLoading = state.isLoading,
        isSuccess = state.isSuccess,
        error = state.sauceError,
        items = state.items,
        onClickGoBack = {
            component.obtainEvent(ShopUserOrdersStore.Intent.OnClickGoBack)
        },
        onClickRetry = {
            component.obtainEvent(ShopUserOrdersStore.Intent.OnClickRetry)
        }
    )

}
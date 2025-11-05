package com.markettwits.sportsouce.shop.item.presentation.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.sportsouce.shop.cart.presentation.page.components.ShopCartItemWidget
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.sportsouce.shop.item.presentation.components.ShopItemPageContent
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore


@Composable
fun ShopItemPageScreen(
    component: ShopItemPageComponent,
    cartComponent: ShopCartPageComponent
) {
    val state by component.state.collectAsState()
        ShopItemPageContent(
            state = state, onClickOption = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickOption(it))
            }, onClickRetry = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickRetry)
            }, onClickGoBack = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickGoBack)
            },
            onClickShare = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickShare)
            },
            onClickAddToFavorite = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickAddToFavorite)
            },
            onClickItem = {
                component.obtainEvent(ShopItemPageStore.Intent.OnClickOption(it))
            },
            cartContent = {
                ShopCartItemWidget(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(bottom = 8.dp),
                    component = cartComponent
                )
            }
        )
}
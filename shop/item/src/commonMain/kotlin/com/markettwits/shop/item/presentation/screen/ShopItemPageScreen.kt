package com.markettwits.shop.item.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.shop.cart.presentation.page.components.ShopCartItemWidget
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.shop.item.presentation.components.ShopItemPageContent
import com.markettwits.shop.item.presentation.store.ShopItemPageStore


@Composable
fun ShopItemPageScreen(
    component: ShopItemPageComponent,
    cartComponent : ShopCartPageComponent
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
        cartContent = {
            ShopCartItemWidget(component = cartComponent)
        }
    )

}
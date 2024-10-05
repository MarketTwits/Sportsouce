package com.markettwits.shop.cart.presentation.page.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponent

@Composable
fun ShopCartItemWidget(
    modifier: Modifier = Modifier,
    component: ShopCartPageComponent
) {
    val state by component.state.collectAsState()

    when (state) {
        is ShopCartPageComponent.State.Empty ->
            ShopCartItemEmpty(
                modifier = modifier,
                onClickAddToCart = component::onClickAddToCart
            )
        is ShopCartPageComponent.State.InCart ->
            ShopCartItemExpanded(
                modifier = modifier,
                countInCart = (state as ShopCartPageComponent.State.InCart).count.toInt(),
                quantity = (state as ShopCartPageComponent.State.InCart).quantity,
                onClickCart = component::onClickCart,
                onClickRemoveFromCart = component::onClickRemove,
                onClickAddToCart = component::onClickAddToCart
            )
    }

}
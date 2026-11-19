package com.markettwits.sportsouce.shop.cart.presentation.page.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponent

@Composable
fun ShopCartItemWidget(
    modifier: Modifier = Modifier,
    component: ShopCartPageComponent,
    enabled: Boolean = true,
    available: Boolean = true,
) {
    val state by component.state.collectAsState()

    when (state) {
        is ShopCartPageComponent.State.Empty ->
            ShopCartItemEmpty(
                modifier = modifier,
                onClickAddToCart = component::onClickAddToCart,
                enabled = enabled && available,
                available = available
            )
        is ShopCartPageComponent.State.InCart ->
            state.apply {
                ShopCartItemExpanded(
                    modifier = modifier,
                    countInCart = (state as ShopCartPageComponent.State.InCart).count.toInt(),
                    quantity = (state as ShopCartPageComponent.State.InCart).quantity,
                    isIncreaseAvailable = enabled && (state as ShopCartPageComponent.State.InCart).isIncreaseAvailable,
                    enabled = enabled,
                    onClickCart = component::onClickCart,
                    onClickRemoveFromCart = component::onClickRemove,
                    onClickAddToCart = component::onClickAddToCart
                )
            }

    }

}

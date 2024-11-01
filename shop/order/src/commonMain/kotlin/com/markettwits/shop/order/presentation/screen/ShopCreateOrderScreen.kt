package com.markettwits.shop.order.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.shop.order.presentation.component.ShopCreateOrderComponent
import com.markettwits.shop.order.presentation.components.ShopCreateOrderContent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore

@Composable
fun ShopCreateOrderScreen(
    modifier: Modifier = Modifier,
    component: ShopCreateOrderComponent
) {
    val state by component.state.collectAsState()

    ShopCreateOrderContent(
        state = state,
        onClickDeliveryType = {
            component.obtainEvent(ShopCreateOrderStore.Intent.OnClickChangeDeliveryType(it))
        },
        onClickPaymentType = {
            component.obtainEvent(ShopCreateOrderStore.Intent.OnClickChangePaymentType(it))
        },
        onClickGoBack = {
            component.obtainEvent(ShopCreateOrderStore.Intent.OnClickGoBack)
        }
    )
}
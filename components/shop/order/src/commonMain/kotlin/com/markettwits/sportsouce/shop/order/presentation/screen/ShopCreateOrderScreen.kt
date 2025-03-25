package com.markettwits.sportsouce.shop.order.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponent
import com.markettwits.sportsouce.shop.order.presentation.components.ShopCreateOrderContent
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore

@Composable
fun ShopCreateOrderScreen(
    modifier: Modifier = Modifier,
    component: ShopCreateOrderComponent
) {
    val state by component.state.collectAsState()

    ShopCreateOrderContent(
        modifier = modifier,
        state = state,
        onClickDeliveryType = {
            component.obtainEvent(ShopCreateOrderStore.Intent.ShopOrderOptionsIntent.OnClickChangeDeliveryType(it))
        },
        onClickPaymentType = {
            component.obtainEvent(ShopCreateOrderStore.Intent.ShopOrderOptionsIntent.OnClickChangePaymentType(it))
        },
        onClickGoBack = {
            component.obtainEvent(ShopCreateOrderStore.Intent.OnClickGoBack)
        },
        onClickCreateOrder = {
            component.obtainEvent(ShopCreateOrderStore.Intent.ShopCreateOrderIntent. OnClickCreateOrder)
        },
        onChangeRecipient = {
            component.obtainEvent(
                ShopCreateOrderStore.Intent.ShopRecipientIntent.OnChangeShopRecipientFields(it)
            )
        },
        onClickShopRecipientSheet = {
            component.obtainEvent(
                ShopCreateOrderStore.Intent.ShopRecipientIntent
                .OnClickChangeRecipientBottomSheetState)
        },
        onClickChangeRecipient = {
            component.obtainEvent(
                ShopCreateOrderStore.Intent.ShopRecipientIntent
                .OnClickChangeRecipient)
        },
        onConsumedMessage = {
            component.obtainEvent(ShopCreateOrderStore.Intent.ShopCreateOrderIntent.OnConsumedMessage)
        }

    )
}
package com.markettwits.shop.order.presentation.store

import com.markettwits.IntentAction
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.order.domain.ShopOrderRepository
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State

class ShopCreateOrderExecutor(
    orderRepository: ShopOrderRepository,
    cartRepository: ShopCartRepository,
    intentAction: IntentAction
) : ShopCreateOrderExecutorHandler(orderRepository,cartRepository,intentAction) {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.ShopOrderOptionsIntent.OnClickChangeDeliveryType -> {
                if (intent.deliveryType is ShopDeliveryType.Delivery) {
                    onClickChangePaymentType(
                        state = getState(),
                        paymentType = ShopPaymentType.Online
                    )
                }
                onClickChangeDeliveryType(
                    state = getState(),
                    deliveryType = intent.deliveryType
                )
            }

            is Intent.ShopOrderOptionsIntent.OnClickChangePaymentType -> onClickChangePaymentType(
                state = getState(),
                paymentType = intent.paymentType
            )

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.ShopCreateOrderIntent.OnClickCreateOrder ->
                onClickCreateOrder(getState())

            is Intent.ShopCreateOrderIntent.OnConsumedMessage ->
                onConsumedCreateOrderMessage(getState())

            is Intent.ShopRecipientIntent.OnClickChangeRecipient ->
                onClickUpdateRecipient(state = getState().shopRecipientState)

            is Intent.ShopRecipientIntent.OnClickChangeRecipientBottomSheetState ->
                onClickChangeRecipientBottomSheetState(getState().shopRecipientState)

            is Intent.ShopRecipientIntent.OnChangeShopRecipientFields ->
                onChangeShopRecipientFields(getState().shopRecipientState,intent.recipient)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        obtainShopRecipient(getState())
        obtainCheckOrder(getState()){
            obtainShopOrderPrice(getState())
        }
    }
}

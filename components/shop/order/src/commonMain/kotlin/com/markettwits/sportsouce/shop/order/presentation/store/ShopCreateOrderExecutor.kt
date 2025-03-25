package com.markettwits.sportsouce.shop.order.presentation.store

import com.markettwits.IntentAction
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.order.domain.ShopOrderRepository
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.Label

class ShopCreateOrderExecutor(
    orderRepository: ShopOrderRepository,
    cartRepository: ShopCartRepository,
    intentAction: IntentAction
) : ShopCreateOrderExecutorHandler(orderRepository,cartRepository,intentAction) {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ShopOrderOptionsIntent.OnClickChangeDeliveryType -> {
                if (intent.deliveryType is ShopDeliveryType.Delivery) {
                    onClickChangePaymentType(
                        state = state(),
                        paymentType = ShopPaymentType.Online
                    )
                }
                onClickChangeDeliveryType(
                    state = state(),
                    deliveryType = intent.deliveryType
                )
            }

            is Intent.ShopOrderOptionsIntent.OnClickChangePaymentType -> onClickChangePaymentType(
                state = state(),
                paymentType = intent.paymentType
            )

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.ShopCreateOrderIntent.OnClickCreateOrder ->
                onClickCreateOrder(state())

            is Intent.ShopCreateOrderIntent.OnConsumedMessage ->
                onConsumedCreateOrderMessage(state())

            is Intent.ShopRecipientIntent.OnClickChangeRecipient ->
                onClickUpdateRecipient(state = state().shopRecipientState)

            is Intent.ShopRecipientIntent.OnClickChangeRecipientBottomSheetState ->
                onClickChangeRecipientBottomSheetState(state().shopRecipientState)

            is Intent.ShopRecipientIntent.OnChangeShopRecipientFields ->
                onChangeShopRecipientFields(state().shopRecipientState,intent.recipient)
        }
    }

    override fun executeAction(action: Unit) {
        obtainShopRecipient(state())
        obtainCheckOrder(state()){
            obtainShopOrderPrice(state())
        }
    }
}

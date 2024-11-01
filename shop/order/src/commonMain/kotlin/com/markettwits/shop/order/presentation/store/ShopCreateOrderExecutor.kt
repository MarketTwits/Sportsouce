package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Message

class ShopCreateOrderExecutor : ShopCreateOrderExecutorHandler() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickChangeDeliveryType -> {
                if(intent.deliveryType is ShopDeliveryType.Delivery){
                    onClickChangePaymentType(getState(), ShopPaymentType.Online)
                }
                onClickChangeDeliveryType(getState(),intent.deliveryType)
            }
            is Intent.OnClickChangePaymentType -> onClickChangePaymentType(getState(),intent.paymentType)
            is Intent.OnClickGoBack -> publish(Label.GoBack)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        obtainShopOrderPrice(getState())
    }
}

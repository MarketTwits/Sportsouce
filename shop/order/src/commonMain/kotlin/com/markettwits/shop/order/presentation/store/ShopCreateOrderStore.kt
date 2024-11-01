package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.cloud_shop.model.order.request.ShopOrderItem
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopOrderPrice
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State

interface ShopCreateOrderStore : Store<Intent, State, Label> {

    data class State(
        val shopOrderPrice: ShopOrderPrice,
        val shopRecipient: ShopRecipient,
        val deliveryType : SelectedOption<ShopDeliveryType>,
        val paymentType: SelectedOption<ShopPaymentType>,
        val shopOrderItems : List<ShopItemCart>
    ){
        data class SelectedOption<T>(
            val selectedOption : T,
            val options : List<T>
        )
    }

    sealed interface Intent{
        data object OnClickGoBack : Intent
        data class OnClickChangeDeliveryType(val deliveryType: ShopDeliveryType) : Intent
        data class OnClickChangePaymentType(val paymentType: ShopPaymentType) : Intent
    }

    sealed interface Message{

        data class UpdateOrderState(val order: State) : Message

    }

    sealed interface Label{

        data object GoBack : Label
    }

    companion object{
        val DEFAULT_SHOP_ORDER_STATE = State(
            shopOrderPrice = ShopOrderPrice(0,0,0),
            shopRecipient = ShopRecipient("Иванов Иван Иванович","+7 913 003 76 75"),
            deliveryType = State.SelectedOption(ShopDeliveryType.Delivery, listOf(ShopDeliveryType.Delivery, ShopDeliveryType.Pickup)),
            paymentType = State.SelectedOption(ShopPaymentType.Online, listOf(ShopPaymentType.Online, ShopPaymentType.Cache)),
            shopOrderItems = emptyList())

    }

}

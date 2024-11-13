package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopItemOrderResult
import com.markettwits.shop.order.domain.model.ShopOrderPrice
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State

interface ShopCreateOrderStore : Store<Intent, State, Label> {

    data class State(
        val shopOrderPrice: ShopOrderPrice,
        val shopRecipientState: ShopRecipientState,
        val shopCreateOrderState : ShopCreateOrderButtonState,
        val deliveryType : SelectedOption<ShopDeliveryType>,
        val paymentType: SelectedOption<ShopPaymentType>,
        val shopOrderItems : List<ShopItemCart>,
        val shopOrderResultItems : List<ShopItemOrderResult>,
        val message : StateEventWithContent<EventContent>
    ){
        data class SelectedOption<T>(
            val selectedOption : T,
            val options : List<T>
        )
        data class ShopRecipientState(
            val isExpanded : Boolean,
            val isAvailable : Boolean,
            val currentShopRecipient: ShopRecipient,
            val mutableShopRecipient : ShopRecipient,
            val message : String,
        )
        data class ShopCreateOrderButtonState(
            val isLoading: Boolean,
            val isAvailable : Boolean,
            val message: String,
        )
    }

    sealed interface Intent{

        sealed interface ShopRecipientIntent : Intent{
            data class OnChangeShopRecipientFields(val recipient: ShopRecipient) : ShopRecipientIntent
            data object OnClickChangeRecipient : ShopRecipientIntent
            data object OnClickChangeRecipientBottomSheetState : ShopRecipientIntent
        }
        sealed interface ShopCreateOrderIntent : Intent{
            data object OnConsumedMessage : ShopCreateOrderIntent
            data object OnClickCreateOrder : ShopCreateOrderIntent
        }
        sealed interface ShopOrderOptionsIntent : Intent{
            data class OnClickChangeDeliveryType(val deliveryType: ShopDeliveryType) : ShopOrderOptionsIntent
            data class OnClickChangePaymentType(val paymentType: ShopPaymentType) : ShopOrderOptionsIntent
        }
        data object OnClickGoBack : Intent
    }

    sealed interface Message{
        data class UpdateOrderState(val order: State) : Message
        data class UpdateShopRecipientState(val shopRecipientState: State.ShopRecipientState) : Message
        data class UpdateCreateOrderStatus(val shopCreateOrderState: State.ShopCreateOrderButtonState) : Message
        data class UpdateMessage(val message : StateEventWithContent<EventContent>) : Message
        data class UpdateShopOrderResultItems(val items : List<ShopItemOrderResult>) : Message
    }

    sealed interface Label{

        data object GoBack : Label
    }

    companion object{

        private val DEFAULT_SHOP_RECIPIENT_STATE = State.ShopRecipientState(
            isExpanded = false,
            isAvailable = false,
            currentShopRecipient = ShopRecipient("Иванов Иван Иванович","+7 913 003 76 75"),
            mutableShopRecipient = ShopRecipient("Иванов Иван Иванович","+7 913 003 76 75"),
            message = ""
        )

        private val DEFAULT_SHOP_CREATE_ORDER_STATE = State.ShopCreateOrderButtonState(
            isLoading = false,
            isAvailable = false,
            message = "Оформить"
        )

        val DEFAULT_SHOP_ORDER_STATE = State(
            shopOrderPrice = ShopOrderPrice(0,0,0),
            shopRecipientState = DEFAULT_SHOP_RECIPIENT_STATE,
            deliveryType = State.SelectedOption(ShopDeliveryType.Delivery, listOf(ShopDeliveryType.Delivery, ShopDeliveryType.Pickup)),
            paymentType = State.SelectedOption(ShopPaymentType.Online, listOf(ShopPaymentType.Online, ShopPaymentType.Cache)),
            message = consumed(),
            shopOrderItems = emptyList(),
            shopOrderResultItems = emptyList(),
            shopCreateOrderState = DEFAULT_SHOP_CREATE_ORDER_STATE
        )

    }

}

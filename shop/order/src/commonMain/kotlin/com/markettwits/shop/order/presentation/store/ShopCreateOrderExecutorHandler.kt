package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.domain.calculateTotalCost
import com.markettwits.shop.cart.domain.formatPrice
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore
import com.markettwits.shop.order.domain.ShopOrderRepository
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopOrderPrice
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Message
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State
import kotlinx.coroutines.launch

abstract class ShopCreateOrderExecutorHandler(
    private val repository: ShopOrderRepository
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    protected fun onClickChangePaymentType(state: State, paymentType: ShopPaymentType) {
        dispatch(
            Message.UpdateOrderState(
                order = state.copy(paymentType = state.paymentType.copy(selectedOption = paymentType))
            )
        )
    }

    protected fun onClickChangeDeliveryType(state: State, deliveryType: ShopDeliveryType) {
        dispatch(
            Message.UpdateOrderState(
                order = state.copy(deliveryType = state.deliveryType.copy(selectedOption = deliveryType))
            )
        )
    }

    protected fun onClickUpdateRecipient(state : State, recipient: ShopRecipient){
        dispatch(Message.UpdateOrderState(state.copy(shopRecipient = recipient)))
    }


    protected fun obtainShopOrderPrice(state: State){
        val price = ShopOrderPrice(
            totalPrice = state.shopOrderItems.sumOf { it.calculateTotalCost() },
            productCount = state.shopOrderItems.sumOf { it.count },
            totalDiscount = calculateTotalDiscount(state.shopOrderItems)
        )
        dispatch(Message.UpdateOrderState(state.copy(shopOrderPrice = price)))
    }

    protected fun obtainShopRecipient(state : State){
        scope.launch {
            val shopRecipient = repository.getUserInfo()
            dispatch(Message.UpdateOrderState(state.copy(shopRecipient = shopRecipient)))
        }
    }

    private fun calculateTotalDiscount(cart: List<ShopItemCart>): Int {
        var totalDiscount = 0
        for (item in cart) {
            if (item.numberPreviousPrice != null && item.numberPreviousPrice!! > item.numberPrice) {
                val discountPerItem = item.numberPreviousPrice!! - item.numberPrice
                totalDiscount += discountPerItem * item.count
            }
        }
        return if (totalDiscount <= 0) 0 else totalDiscount
    }
}
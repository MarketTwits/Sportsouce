package com.markettwits.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.domain.formatPrice
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Intent
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Label
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.State
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Message
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class ShopCartExecutor(
    private val repository: ShopCartRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickDecrease -> onClickDecrease(intent.item)
            is Intent.OnClickIncrease -> onClickIncrease(intent.item)
            is Intent.OnClickItem -> publish(Label.GoToShopItem(intent.item))
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickChangePaymentType -> onClickChangePaymentType(getState())
            is Intent.OnClickCreateOrder -> {}
            is Intent.Init -> executeAction(Unit) { getState() }
            is Intent.OnClickChangeDeliveryWay -> onClickChangeDeliveryWay(getState())
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        repository.observe().onEach {
            updateState(it, getState())
        }.launchIn(scope)
    }

    private fun onClickDecrease(itemCart: ShopItemCart) {
        scope.launch {
            repository.remove(itemCart)
        }
    }

    private fun onClickIncrease(itemCart: ShopItemCart) {
        if (itemCart.quantity > itemCart.count) {
            scope.launch {
                repository.add(itemCart)
            }
        }
    }

    private fun onClickChangePaymentType(state: State) {
        val value = state.order.payByCache
        dispatch(Message.UpdateOrder(state.order.copy(payByCache = !value)))
    }

    private fun onClickChangeDeliveryWay(state: State) {
        val value = state.order.isDelivery
        dispatch(
            Message.UpdateOrder(
                state.order.copy(
                    isDelivery = !value,
                    payByCache = value
                )
            )
        )
    }

    private fun updateState(items: List<ShopItemCart>, state: State) {
        if (items.isEmpty()) {
            publish(Label.GoBack)
        } else {
            dispatch(Message.UpdateOrder(mapOrder(items, state)))
            dispatch(Message.UpdateItemsInCart(items))
        }
    }

    private fun mapOrder(items: List<ShopItemCart>, state: State): State.Order {

        val totalCost = items.sumOf { item ->
            item.numberPrice * item.count
        }

        val itemsCount = items.sumOf {
            it.count
        }.toString()

        val value = state.order.copy(
            itemsCount = itemsCount,
            totalCost = totalCost.formatPrice(),
            discount = calculateTotalDiscount(items)
        )
        return value
    }

    private fun calculateTotalDiscount(cart: List<ShopItemCart>): String {
        var totalDiscount = 0
        for (item in cart) {
            if (item.numberPreviousPrice != null && item.numberPreviousPrice > item.numberPrice) {
                val discountPerItem = item.numberPreviousPrice - item.numberPrice
                totalDiscount += discountPerItem * item.count
            }
        }
        return if (totalDiscount <= 0) "" else totalDiscount.toString()
    }

}

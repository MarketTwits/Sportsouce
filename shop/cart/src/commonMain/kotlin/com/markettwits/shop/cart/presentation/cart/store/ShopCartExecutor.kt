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
            is Intent.OnClickDelete -> onClickDelete(intent.item)
            is Intent.OnClickItem -> publish(Label.GoToShopItem(intent.item))
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickCreateOrder -> publish(Label.GoOrderScreen(getState().items))
            is Intent.Init -> executeAction(Unit) { getState() }
            is Intent.OnClickGoAuth -> publish(Label.GoAuth)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        repository.observe().onEach {
            updateState(it, getState())
        }.launchIn(scope)
    }

    private fun onClickDelete(itemCart: ShopItemCart){
        scope.launch {
            repository.remove(itemCart)
        }
    }

    private fun onClickDecrease(itemCart: ShopItemCart) {
        scope.launch {
            repository.remove(itemCart)
        }
    }

    private fun onClickIncrease(itemCart: ShopItemCart) {
        if (itemCart.item.quantity > itemCart.count) {
            scope.launch {
                repository.add(itemCart)
            }
        }
    }

    private fun updateState(items: List<ShopItemCart>, state: State) {
        if (items.isEmpty()) {
            publish(Label.GoBack)
        } else {
            scope.launch {
               dispatch(Message.UpdateCreateOrderAvailable(
                   repository.createOrderIsAvailable().isAvailable())
               )
            }
            dispatch(Message.UpdateItemsInCart(items = items, itemsCount = items.sumOf { it.count }))
        }
    }

}

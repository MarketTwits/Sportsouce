package com.markettwits.sportsouce.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.Intent
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.Label
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.Message
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class ShopCartExecutor(
    private val repository: ShopCartRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickDecrease -> onClickDecrease(intent.item)
            is Intent.OnClickIncrease -> onClickIncrease(intent.item)
            is Intent.OnClickDelete -> onClickDelete(intent.item)
            is Intent.OnClickItem -> publish(Label.GoToShopItem(intent.item))
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickCreateOrder -> publish(Label.GoOrderScreen(state().items))
            is Intent.Init -> executeAction(Unit)
            is Intent.OnClickGoAuth -> publish(Label.GoAuth)
        }
    }

    override fun executeAction(action: Unit) {
        updateCreateOrderAvailable()
        repository.observe().onEach {
            updateState(it)
        }.launchIn(scope)
    }

    private fun onClickDelete(itemCart: ShopItemCart) =
        scope.launch {
            repository.delete(itemCart)
        }


    private fun onClickDecrease(itemCart: ShopItemCart) =
        scope.launch {
            repository.remove(itemCart)
        }

    private fun onClickIncrease(itemCart: ShopItemCart) {
        if (itemCart.item.quantity > itemCart.count) {
            scope.launch {
                repository.add(itemCart)
            }
        }
    }

    private fun updateCreateOrderAvailable(){
        scope.launch {
            val result = repository.createOrderAvailable()
            dispatch(Message.UpdateCreateOrderAvailable(result.isSuccess))
        }
    }

    private fun updateState(items: List<ShopItemCart>) {
        if (items.isEmpty()) {
            publish(Label.GoBack)
        } else {
            dispatch(Message.UpdateItemsInCart(items = items, itemsCount = items.sumOf { it.count }))
        }
    }
}

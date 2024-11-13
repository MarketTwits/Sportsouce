package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Message
import kotlinx.coroutines.launch

class ShopUserOrdersExecutor(
    private val repository: ShopUserOrdersRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            else -> TODO()
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
       launch()
    }

    private fun launch(){
        scope.launch {
            repository.getUserOrders().fold(
                onSuccess = {

                }, onFailure = {

                }
            )
        }
    }
}

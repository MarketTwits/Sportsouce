package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Message
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ShopUserOrdersExecutor(
    private val repository: ShopUserOrdersRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickRetry -> launch(true)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(false)
    }

    private fun launch(forced : Boolean) {
        scope.launch {
            dispatch(Message.Loading)
            repository.getUserOrders(forced)
                .catch {
                    dispatch(Message.Error(it.mapToSauceError()))
                    println("---------------------")
                    println(it.message.toString())
                    println("---------------------")
                }
                .collect {
                    dispatch(Message.Loaded(it))
                }
        }
    }
}

package com.markettwits.sportsouce.shop.orders.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.isResponseException
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.crashlitics.api.logging.ExceptionLoggingTracker
import com.markettwits.crashlitics.api.logging.errorShaker
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.Message
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ShopUserOrdersExecutor(
    private val repository: ShopUserOrdersRepository, tracker: ExceptionTracker
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(), ExceptionLoggingTracker {

    override val exceptionTracker: ExceptionTracker = tracker

    override val tag: String = "ShopUserOrdersExecutor"

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickRetry -> launch(true)
        }
    }

    override fun executeAction(action: Unit) {
        launch(false)
    }

    private fun launch(forced : Boolean) {
        scope.launch {
            repository.getUserOrders(forced)
                .onStart {
                    dispatch(Message.Loading)
                }
                .catch {
                    dispatch(Message.Error(it.mapToSauceError()))
                    if (it.isResponseException())
                        errorShaker(error = it) { "fail when try get orders" }
                }
                .collect {
                    dispatch(Message.Loaded(it))
                }
        }
    }
}

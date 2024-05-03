package com.markettwits.start.register.presentation.order.presentation.store

import com.markettwits.IntentAction
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.common.EventContentTest
import com.markettwits.start.register.presentation.common.triggered
import com.markettwits.start.register.presentation.order.domain.OrderStatement
import com.markettwits.start.register.presentation.order.domain.interactor.OrderInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderStoreExecutorHandleBase(
    private val interactor: OrderInteractor,
    private val intentAction: IntentAction
) : OrderStoreExecutorHandle {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun changeButtonState(
        state: OrderStore.State,
        enabled: Boolean,
        isLoading: Boolean
    ): OrderStore.State {
        val new = state.button.copy(isLoading = isLoading, isEnabled = enabled)
        return state.copy(button = new)
    }

    override fun changePaymentType(state: OrderStore.State, payNow: Boolean): OrderStore.State {
        val update = state.orderStatement?.copy(payNow = payNow)
        return state.copy(orderStatement = update)
    }

    override fun onClickRegistry(
        state: OrderStore.State,
        distanceItem: DistanceItem,
        startId: Int,
        isSuccess: () -> Unit,
        newState: (OrderStore.State) -> Unit,
    ) {
        scope.launch {
            interactor.valid(checkNotNull(state.orderStatement)).fold(onSuccess = {
                newState(state.copy(button = changeButtonState(state, false, true).button))
                val result = interactor.registry(
                    checkNotNull(state.orderStatement),
                    distanceItem,
                    startId
                )
                if (result.isError) {
                    val message = showMessage(state, false, result.message)
                    val button = changeButtonState(state, true, false)
                    newState(state.copy(event = message.event, button = button.button))
                }
                if (result.isSuccess) {
                    if (result.paymentUrl.isNotEmpty()) {
                        val message = showMessage(state, true, result.paymentUrl)
                        val button = changeButtonState(state, true, false)
                        intentAction.openWebPage(result.paymentUrl)
                        newState(state.copy(event = message.event, button = button.button))
                    } else {
                        val message =
                            showMessage(state, true, "Вы успешно зарегестрировались на старт")
                        val button = changeButtonState(state, true, false)
                        newState(state.copy(event = message.event, button = button.button))
                    }
                    isSuccess()
                }
            }, onFailure = {
                val message = showMessage(state, false, it.message.toString())
                val button = changeButtonState(state, true, false)
                newState(state.copy(event = message.event, button = button.button))
            })
        }
    }

    override fun showMessage(
        state: OrderStore.State,
        success: Boolean,
        message: String
    ): OrderStore.State =
        state.copy(
            event = triggered(
                EventContentTest(
                    success = success,
                    message = message
                )
            )
        )

    override fun onClickCheckRules(state: OrderStore.State): OrderStore.State {
        val prev = state.orderStatement!!.copy(checkPolitics = !state.orderStatement.checkPolitics)
        val button = changeButtonState(
            state,
            enabled = prev.checkPolitics,
            isLoading = state.button.isLoading
        )
        return button.copy(orderStatement = prev)
    }

    override fun applyMember(
        currentState: OrderStatement,
        startStatement: StartStatement,
        id: Int
    ): OrderStatement {
        val updatedMembers = currentState.members.toMutableList()
        if (id in updatedMembers.indices) {
            updatedMembers[id] = startStatement
        } else {
            throw IndexOutOfBoundsException("Error: Index out of bounds")
        }
        return currentState.copy(members = updatedMembers.toList())
    }

    override fun applyPromo(
        state: OrderStore.State,
        promo: String,
        percent: Int
    ): OrderStore.State {
        val order = state.orderStatement!!
        val discountPercent = percent.toDouble() / 100
        val currentPrice = order.orderPrice.total
        val discountAmount = currentPrice * discountPercent
        val discountedPrice = currentPrice - discountAmount
        val newOrderPrice = order.orderPrice.copy(
            discountInCache = discountAmount,
            discountInPercent = percent,
            total = discountedPrice
        )
        val message = showMessage(state, success = true, message = "Промокод $promo применен")
        return state.copy(
            event = message.event,
            orderStatement = state.orderStatement.copy(orderPrice = newOrderPrice, promo = promo)
        )
    }

    override fun onClickUrl(url: String) {
        intentAction.openWebPage(url)
    }
}
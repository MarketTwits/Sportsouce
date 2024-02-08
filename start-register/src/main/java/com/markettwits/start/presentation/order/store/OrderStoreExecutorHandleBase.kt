package com.markettwits.start.presentation.order.store

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderStoreExecutorHandleBase(private val interactor: OrderInteractor) :
    OrderStoreExecutorHandle {
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun changePaymentType(state: OrderStore.State, payNow: Boolean): OrderStore.State {
        val update = state.orderStatement?.copy(payNow = payNow)
        return state.copy(orderStatement = update)
    }

    override fun onClickRegistry(
        state: OrderStore.State,
        distanceItem: DistanceItem,
        startId: Int,
        show: (OrderStore.State) -> Unit
    ) {
        scope.launch {
            interactor.valid(checkNotNull(state.orderStatement)).fold(onSuccess = {
                val result = interactor.registry(
                    checkNotNull(state.orderStatement),
                    distanceItem,
                    startId
                )
                if (result.isError) {
                    val message = showMessage(state, false, result.message)
                    show(message)
                }
                if (result.isSuccess) {
                    if (result.paymentUrl.isNotEmpty()) {
                        show(showMessage(state, true, result.paymentUrl))
                    } else {
                        show(showMessage(state, true, "Вы успешно зарегестрировались на старт"))
                    }
                }
            }, onFailure = {
                show(showMessage(state, false, it.message.toString()))
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
                EventContent(
                    success = success,
                    message = message
                )
            )
        )


    override fun onClickCheckRules(state: OrderStatement): OrderStatement =
        state.copy(checkPolitics = !state.checkPolitics)


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
        currentState: OrderStatement,
        promo: String,
        percent: Int
    ): OrderStatement {
        val discountPercent = percent.toDouble() / 100
        val currentPrice = currentState.orderPrice.total
        val discountAmount = currentPrice * discountPercent
        val discountedPrice = currentPrice - discountAmount
        val newOrderPrice = currentState.orderPrice.copy(
            discountInCache = discountAmount,
            discountInPercent = percent,
            total = discountedPrice
        )
        return currentState.copy(orderPrice = newOrderPrice, promo = promo)
    }
}
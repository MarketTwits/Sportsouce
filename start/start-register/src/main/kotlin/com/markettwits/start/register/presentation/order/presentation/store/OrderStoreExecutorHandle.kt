package com.markettwits.start.register.presentation.order.presentation.store

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.order.domain.OrderStatement

interface OrderStoreExecutorHandle {
    fun changeButtonState(
        state: OrderStore.State,
        enabled: Boolean,
        isLoading: Boolean
    ): OrderStore.State

    fun changePaymentType(state: OrderStore.State, payNow: Boolean): OrderStore.State
    fun onClickRegistry(
        state: OrderStore.State,
        distanceItem: DistanceItem,
        startId: Int,
        newState: (OrderStore.State) -> Unit
    )

    fun showMessage(state: OrderStore.State, success: Boolean, message: String): OrderStore.State
    fun onClickCheckRules(state: OrderStore.State): OrderStore.State
    fun applyMember(
        currentState: OrderStatement,
        startStatement: StartStatement,
        id: Int
    ): OrderStatement

    fun applyPromo(
        state: OrderStore.State,
        promo: String,
        percent: Int
    ): OrderStore.State
}
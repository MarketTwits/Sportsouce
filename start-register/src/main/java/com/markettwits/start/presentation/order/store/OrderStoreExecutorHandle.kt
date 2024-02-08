package com.markettwits.start.presentation.order.store

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement

interface OrderStoreExecutorHandle {
    fun changePaymentType(state: OrderStore.State, payNow: Boolean): OrderStore.State
    fun onClickRegistry(
        state: OrderStore.State,
        distanceItem: DistanceItem,
        startId: Int,
        show: (OrderStore.State) -> Unit
    )

    fun showMessage(state: OrderStore.State, success: Boolean, message: String): OrderStore.State
    fun onClickCheckRules(state: OrderStatement): OrderStatement
    fun applyMember(
        currentState: OrderStatement,
        startStatement: StartStatement,
        id: Int
    ): OrderStatement

    fun applyPromo(
        currentState: OrderStatement,
        promo: String,
        percent: Int
    ): OrderStatement
}
package com.markettwits.start.presentation.order.component

import com.markettwits.start.presentation.order.store.OrderStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface OrderComponentComponent {
    val labels: Flow<OrderStore.Label>
    val model: StateFlow<OrderStore.State>
    fun obtainEvent(event: OrderStore.Intent)
}

package com.markettwits.start.presentation.order.presentation.component

import com.markettwits.start.presentation.order.presentation.store.OrderStore
import kotlinx.coroutines.flow.StateFlow

interface OrderComponentComponent {
    val model: StateFlow<OrderStore.State>
    fun obtainEvent(event: OrderStore.Intent)
}

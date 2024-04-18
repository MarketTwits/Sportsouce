package com.markettwits.start.register.presentation.order.presentation.component

import com.markettwits.start.register.presentation.order.presentation.store.OrderStore
import kotlinx.coroutines.flow.StateFlow

interface OrderComponentComponent {
    val model: StateFlow<OrderStore.State>
    fun obtainEvent(event: OrderStore.Intent)
}

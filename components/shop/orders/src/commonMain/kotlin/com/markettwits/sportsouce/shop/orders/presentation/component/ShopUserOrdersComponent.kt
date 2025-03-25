package com.markettwits.sportsouce.shop.orders.presentation.component

import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStore
import kotlinx.coroutines.flow.StateFlow

interface ShopUserOrdersComponent {

    val state: StateFlow<ShopUserOrdersStore.State>

    fun obtainEvent(intent: ShopUserOrdersStore.Intent)

    interface Outputs{
        fun goBack()
    }

}

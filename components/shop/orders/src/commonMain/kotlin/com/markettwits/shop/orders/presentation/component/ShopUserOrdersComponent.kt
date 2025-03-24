package com.markettwits.shop.orders.presentation.component

import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore
import kotlinx.coroutines.flow.StateFlow

interface ShopUserOrdersComponent {

    val state: StateFlow<ShopUserOrdersStore.State>

    fun obtainEvent(intent: ShopUserOrdersStore.Intent)

    interface Outputs{
        fun goBack()
    }

}

package com.markettwits.shop.order.presentation.component

import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore
import kotlinx.coroutines.flow.StateFlow

interface ShopCreateOrderComponent {

    val state : StateFlow<ShopCreateOrderStore.State>

    fun obtainEvent(intent: ShopCreateOrderStore.Intent)

    interface Outputs{
        fun goBack()
    }

}

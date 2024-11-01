package com.markettwits.shop.search.presentation.component

import com.markettwits.shop.search.presentation.store.ShopSearchStore
import kotlinx.coroutines.flow.StateFlow

interface ShopSearchComponent {

    val state : StateFlow<ShopSearchStore.State>

    fun obtainEvent(intent : ShopSearchStore.Intent)

    interface Outputs{
        fun goBack()
        fun onApplyQuery(query : String)
    }
}
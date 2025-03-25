package com.markettwits.sportsouce.shop.filter.presentation.component

import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterResult
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore
import kotlinx.coroutines.flow.StateFlow

interface ShopFilterComponent {

    val state: StateFlow<ShopFilterStore.State>

    fun obtainEvent(intent: ShopFilterStore.Intent)

    interface Output {

        fun goBack()

        fun applyFilter(result: ShopFilterResult)
    }

}

package com.markettwits.shop.catalog.presentation.component

import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import kotlinx.coroutines.flow.StateFlow

interface ShopCatalogComponent {

    val state: StateFlow<ShopCatalogStore.State>

    fun obtainEvent(intent: ShopCatalogStore.Intent)

    interface Outputs {
        fun onClickShopItem(id: String)

        fun goBack()

        fun goFilter(filterState: ShopFilterStore.State?)
    }
}

package com.markettwits.shop.catalog.presentation.component

import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.domain.model.ShopItem
import kotlinx.coroutines.flow.StateFlow

interface ShopCatalogComponent {

    val state: StateFlow<ShopCatalogStore.State>

    fun obtainEvent(intent: ShopCatalogStore.Intent)

    interface Outputs {

        fun onClickShopItem(item: ShopItem)

        fun goBack()

        fun goFilter()

        fun goSearch(query : String)
    }
}

package com.markettwits.shop.catalog.presentation.component

import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import kotlinx.coroutines.flow.StateFlow

interface CardsComponent {

    val state: StateFlow<ShopCatalogStore.State>

    fun obtainEvent(intent: ShopCatalogStore.Intent)

    interface Outputs {
        fun onClickShopItem(id: String)
        fun goBack()
    }
}
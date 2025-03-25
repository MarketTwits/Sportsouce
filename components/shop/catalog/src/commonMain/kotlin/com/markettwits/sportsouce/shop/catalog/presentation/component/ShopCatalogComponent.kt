package com.markettwits.sportsouce.shop.catalog.presentation.component

import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import kotlinx.coroutines.flow.StateFlow

interface ShopCatalogComponent {

    val state: StateFlow<ShopCatalogStore.State>

    val listener: BottomBarVisibilityListener

    fun obtainEvent(intent: ShopCatalogStore.Intent)

    interface Outputs {

        fun onClickShopItem(item: ShopItem)

        fun goBack()

        fun goFilter()

        fun goSearch(query : String)
    }
}

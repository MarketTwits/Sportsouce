package com.markettwits.shop.catalog.presentation.component

import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import kotlinx.coroutines.flow.StateFlow

interface ShopCatalogComponent {

    val state: StateFlow<ShopCatalogStore.State>

    fun obtainEvent(intent: ShopCatalogStore.Intent)

    interface Outputs {
        fun onClickShopItem(item: ShopItem)

        fun goBack()

        fun goFilter(filterState: ShopFilterStore.State?)

        fun goSearch(query : String)

        fun onClickCategory(categoryItem: ShopCategoryItem)

    }
}

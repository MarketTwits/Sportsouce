package com.markettwits.shop.item.presentation.component

import com.markettwits.shop.item.domain.models.ShopPageItem
import com.markettwits.shop.item.presentation.store.ShopItemPageStore
import kotlinx.coroutines.flow.StateFlow

interface ShopItemPageComponent {

    val state: StateFlow<ShopItemPageStore.State>

    fun obtainEvent(intent: ShopItemPageStore.Intent)

    interface Output {
        fun goBack()
        fun updateItem(item : ShopPageItem)
    }

}
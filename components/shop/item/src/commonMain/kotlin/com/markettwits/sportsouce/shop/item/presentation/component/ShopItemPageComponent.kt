package com.markettwits.sportsouce.shop.item.presentation.component

import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore
import kotlinx.coroutines.flow.StateFlow

interface ShopItemPageComponent {

    val state: StateFlow<ShopItemPageStore.State>

    fun obtainEvent(intent: ShopItemPageStore.Intent)

    interface Output {

        fun goBack()

        fun updateItem(item : ShopItem)
    }

}
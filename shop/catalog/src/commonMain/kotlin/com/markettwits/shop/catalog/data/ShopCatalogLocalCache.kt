package com.markettwits.shop.catalog.data

import com.markettwits.shop.catalog.domain.models.ShopItem

data object ShopCatalogLocalCache {

    private val innerItems = emptyList<ShopItem>().toMutableList()

    fun write(items: List<ShopItem>) {
        innerItems.addAll(items)
    }

    fun fetch(): List<ShopItem> = innerItems
}
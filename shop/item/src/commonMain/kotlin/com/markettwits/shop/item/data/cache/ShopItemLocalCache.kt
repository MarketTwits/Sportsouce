package com.markettwits.shop.item.data.cache

import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions

class ShopItemLocalCache {

    private val items: MutableMap<String, Pair<ShopItem, List<ShopExtraOptions>>> =
        emptyMap<String, Pair<ShopItem, List<ShopExtraOptions>>>().toMutableMap()

    fun add(shopItem: ShopItem, options: List<ShopExtraOptions>) {
        items[shopItem.id] = Pair(shopItem, options)
    }

    fun fetch(uuid: String): Pair<ShopItem, List<ShopExtraOptions>>? = items[uuid]

}
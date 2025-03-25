package com.markettwits.sportsouce.shop.item.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.product.ProductRemote
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions

interface ShopPageItemMapper {

    fun map(products: ProductRemote): Pair<ShopItem,List<ShopExtraOptions>>

    fun mapWithPrevOptions(
        newValue : Pair<ShopItem,List<ShopExtraOptions>>,
        prevValue: Pair<ShopItem,List<ShopExtraOptions>>
    ) : Pair<ShopItem, List<ShopExtraOptions>>

}
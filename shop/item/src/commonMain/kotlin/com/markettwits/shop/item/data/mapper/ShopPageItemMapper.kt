package com.markettwits.shop.item.data.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions

interface ShopPageItemMapper {

    fun map(products: ProductRemote): Pair<ShopItem,List<ShopExtraOptions>>

}
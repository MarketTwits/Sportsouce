package com.markettwits.shop.item.domain.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.item.domain.models.ShopPageItem

interface ShopPageItemMapper {

    fun map(products: ProductRemote): ShopPageItem

}
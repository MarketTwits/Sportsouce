package com.markettwits.shop.domain.mapper

import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.shop.domain.model.ShopItem

interface ShopProductsMapper {

    fun map(products: List<Product>): List<ShopItem>

    fun map(product: Product): ShopItem

}
package com.markettwits.sportsouce.shop.domain.mapper

import com.markettwits.sportsouce.shop.cloud.model.product.Product
import com.markettwits.sportsouce.shop.domain.model.ShopItem

interface ShopProductsMapper {

    fun map(products: List<Product>): List<ShopItem>

    fun map(product: Product): ShopItem

}
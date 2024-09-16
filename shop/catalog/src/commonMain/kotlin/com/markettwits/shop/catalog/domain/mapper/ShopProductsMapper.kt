package com.markettwits.shop.catalog.domain.mapper

import com.markettwits.cloud_shop.model.products.ProductsRemoteRow
import com.markettwits.shop.catalog.presentation.cards.ShopItem

interface ShopProductsMapper {

    fun map(products: List<ProductsRemoteRow>): List<ShopItem>

}
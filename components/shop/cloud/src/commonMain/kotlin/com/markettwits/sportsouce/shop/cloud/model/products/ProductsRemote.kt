package com.markettwits.sportsouce.shop.cloud.model.products

import com.markettwits.sportsouce.shop.cloud.model.product.Product
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductsRemote(
    val count: Int,
    val rows: List<Product>
)
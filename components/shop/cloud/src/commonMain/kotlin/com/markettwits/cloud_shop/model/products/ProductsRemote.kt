package com.markettwits.cloud_shop.model.products

import com.markettwits.cloud_shop.model.product.Product
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductsRemote(
    val count: Int,
    val rows: List<Product>
)
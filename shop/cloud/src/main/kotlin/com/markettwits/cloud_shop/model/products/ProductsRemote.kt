package com.markettwits.cloud_shop.model.products

import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.cloud_shop.model.product.ProductRemote
import kotlinx.serialization.Serializable

@Serializable
data class ProductsRemote(
    val count: Int,
    val rows: List<Product>
)
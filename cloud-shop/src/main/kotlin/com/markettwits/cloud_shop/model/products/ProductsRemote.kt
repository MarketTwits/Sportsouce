package com.markettwits.cloud_shop.model.products

import kotlinx.serialization.Serializable

@Serializable
data class ProductsRemote(
    val count: Int,
    val rows: List<ProductsRemoteRow>
)
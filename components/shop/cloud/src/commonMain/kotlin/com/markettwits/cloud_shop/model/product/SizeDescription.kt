package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class SizeDescription(
    val productUuid: String,
    val sizeList: List<Int>?
)
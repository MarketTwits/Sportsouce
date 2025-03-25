package com.markettwits.sportsouce.shop.cloud.model.product

import kotlinx.serialization.Serializable

@Serializable
data class SizeDescription(
    val productUuid: String,
    val sizeList: List<Int>?
)
package com.markettwits.sportsouce.shop.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val `file`: File?,
    val fileId: Int,
    val productId: String
)
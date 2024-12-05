package com.markettwits.cloud_shop.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val `file`: File?,
    val fileId: Int,
    val productId: String
)
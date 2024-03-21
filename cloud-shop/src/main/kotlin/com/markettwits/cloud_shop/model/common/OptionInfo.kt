package com.markettwits.cloud_shop.model.common

import kotlinx.serialization.Serializable

@Serializable
data class OptionInfo(
    val createdAt: String,
    val in_filter: Boolean,
    val name: String,
    val updatedAt: String,
    val uuid: String
)
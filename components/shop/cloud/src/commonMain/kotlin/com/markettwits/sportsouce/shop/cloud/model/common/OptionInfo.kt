package com.markettwits.sportsouce.shop.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class OptionInfo(
    val createdAt: String,
    val in_filter: Boolean,
    val name: String,
    val updatedAt: String,
    val uuid: String,
    val values: List<Value>? = null,
) {
    @Serializable
    data class Value(
        val createdAt: String,
        val name: String,
        val product_option_uuid: String,
        val updatedAt: String,
        val uuid: String,
    )
}
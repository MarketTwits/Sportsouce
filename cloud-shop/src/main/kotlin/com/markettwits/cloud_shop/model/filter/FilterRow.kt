package com.markettwits.cloud_shop.model.filter

import kotlinx.serialization.Serializable

@Serializable
data class FilterRow(
    val createdAt: String,
    val in_filter: Boolean,
    val name: String,
    val updatedAt: String,
    val uuid: String,
    val values: List<FilterValue>
)
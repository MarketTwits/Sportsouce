package com.markettwits.shop.filter.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopOptionInfo(
    val createdAt: String,
    val inFilter: Boolean,
    val name: String,
    val updatedAt: String,
    val uuid: String,
    val values: List<Value>,
) {
    @Serializable
    data class Value(
        val createdAt: String,
        val name: String,
        val productOptionUuid: String,
        val updatedAt: String,
        val uuid: String
    )
}

fun List<ShopOptionInfo.Value>.mapToStringOptions() =
    this.groupBy { it.productOptionUuid }
        .map { (productOptionUuid, values) ->
            val uuids = values.joinToString(",") { it.uuid }
            "$productOptionUuid:$uuids"
        }
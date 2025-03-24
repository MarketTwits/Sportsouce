package com.markettwits.shop.filter.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopOptionInfo(
    val inFilter: Boolean,
    val name: String,
    val uuid: String,
    val values: List<Value>,
) {
    @Serializable
    data class Value(
        val name: String,
        val productOptionUuid: String,
        val uuid: String
    )
}

fun List<ShopOptionInfo.Value>.mapToStringOptions() =
    this.groupBy { it.productOptionUuid }
        .map { (productOptionUuid, values) ->
            val uuids = values.joinToString(",") { it.uuid }
            "$productOptionUuid:$uuids"
        }
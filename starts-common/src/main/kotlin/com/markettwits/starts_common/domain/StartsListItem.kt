package com.markettwits.starts_common.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartsListItem(
    val id: Int,
    val name: String,
    val image: String,
    val date: String,
    val statusCode: StatusCode,
    val place: String,
    val distance: String
) {
    @Serializable
    data class StatusCode(val id: Int, val message: String)
}
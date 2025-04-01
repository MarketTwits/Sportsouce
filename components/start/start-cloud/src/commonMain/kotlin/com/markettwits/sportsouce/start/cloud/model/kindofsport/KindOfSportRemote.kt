package com.markettwits.sportsouce.start.cloud.model.kindofsport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KindOfSportRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<Row>
) {
    @Serializable
    data class Row(
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("updatedAt")
        val updatedAt: String
    )
}

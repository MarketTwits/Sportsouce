package com.markettwits.sportsouce.start.cloud.model.seasons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartSeasonsRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<Row>
) {
    @Serializable
    data class Row(
        @SerialName("id")
        val id: Int,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("is_primary")
        val isPrimary: Boolean,
        @SerialName("name")
        val name: String,
        @SerialName("updatedAt")
        val updatedAt: String
    )
}

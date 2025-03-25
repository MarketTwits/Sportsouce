package com.markettwits.sportsouce.start.cloud.model.seasons

import kotlinx.serialization.Serializable

@Serializable
data class StartSeasonsRemote(
    val count: Int,
    val rows: List<Row>
) {
    @Serializable
    data class Row(
        val createdAt: String,
        val id: Int,
        val is_primary: Boolean,
        val name: String,
        val updatedAt: String
    )
}

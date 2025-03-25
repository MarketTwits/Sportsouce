package com.markettwits.sportsouce.start.cloud.model.kindofsport

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSportRemote(
    val count: Int,
    val rows: List<Row>
) {
    @Serializable
    data class Row(
        val createdAt: String,
        val id: Int,
        val name: String,
        val updatedAt: String
    )
}

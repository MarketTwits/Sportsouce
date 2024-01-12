package com.markettwits.cloud.model.kind_of_sport

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

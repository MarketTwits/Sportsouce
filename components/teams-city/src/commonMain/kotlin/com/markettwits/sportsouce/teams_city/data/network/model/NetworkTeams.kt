package com.markettwits.sportsouce.teams_city.data.network.model

import kotlinx.serialization.Serializable


@Serializable
internal data class NetworkTeams(
    val count: Int,
    val rows: List<Row>
) {
    @Serializable
    internal data class Row(
        val code: Int?,
        val createdAt: String?,
        val id: Int,
        val name: String,
        val updatedAt: String?
    )
}
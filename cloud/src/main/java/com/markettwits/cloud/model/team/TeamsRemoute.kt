package com.markettwits.cloud.model.team

import kotlinx.serialization.Serializable

@Serializable
data class TeamsRemote(
    val count: Int,
    val rows: List<Row>
){
    @Serializable
    data class Row(
        val code: Int?,
        val createdAt: String?,
        val id: Int,
        val name: String,
        val updatedAt: String?
    )
}
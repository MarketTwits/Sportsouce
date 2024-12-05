package com.markettwits.cloud.model.city

import kotlinx.serialization.Serializable

@Serializable
data class CityRemote(
    val count: Int,
    val rows: List<Row>
){
    @Serializable
    data class Row(
        val createdAt: String?,
        val id: Int,
        val name: String,
        val updatedAt: String?
    )
}
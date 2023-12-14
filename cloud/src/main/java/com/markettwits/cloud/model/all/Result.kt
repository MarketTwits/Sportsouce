package com.markettwits.cloud.model.all

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val createdAt: String,
    val fileID: Int,
    val id: Int,
    val name: String,
    val start_id: Int,
    val updatedAt: String
)
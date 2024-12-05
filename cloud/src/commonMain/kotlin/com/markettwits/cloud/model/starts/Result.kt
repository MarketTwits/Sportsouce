package com.markettwits.cloud.model.starts

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val createdAt: String,
    val id: Int,
    val name: String,
    val start_id: Int,
    val updatedAt: String
)
package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkResult(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("updatedAt")
    val updatedAt: String
)
package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkStartFavoritesRequest(
    @SerialName("start_id")
    val startId: Int,
    @SerialName("user_id")
    val userId: String,
)
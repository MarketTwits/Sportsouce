package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkStartsRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<NetworkStartRemoteItem>
)
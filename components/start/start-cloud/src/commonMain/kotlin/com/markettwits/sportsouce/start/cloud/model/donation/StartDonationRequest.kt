package com.markettwits.sportsouce.start.cloud.model.donation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartDonationRequest(
    @SerialName("start_id")
    val startId: Int,
    @SerialName("price")
    val price: Int
)
package com.markettwits.sportsouce.start.cloud.model.donation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartDonationResponse(
    @SerialName("url")
    val url: String
)
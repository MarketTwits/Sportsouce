package com.markettwits.start_cloud.model.donation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartDonationResponse(
    @SerialName("url")
    val url: String
) {
    @Serializable
    data class Donation(
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("id")
        val id: Int,
        @SerialName("order_number")
        val order_number: String,
        @SerialName("price")
        val price: Int,
        @SerialName("sberbank_id")
        val sberbank_id: String,
        @SerialName("start_id")
        val start_id: Int,
        @SerialName("updatedAt")
        val updatedAt: String
    )
}
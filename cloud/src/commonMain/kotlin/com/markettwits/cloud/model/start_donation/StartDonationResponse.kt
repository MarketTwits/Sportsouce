package com.markettwits.cloud.model.start_donation

import kotlinx.serialization.Serializable

@Serializable
data class StartDonationResponse(
    val url: String
) {
    @Serializable
    data class Donation(
        val createdAt: String,
        val id: Int,
        val order_number: String,
        val price: Int,
        val sberbank_id: String,
        val start_id: Int,
        val updatedAt: String
    )
}
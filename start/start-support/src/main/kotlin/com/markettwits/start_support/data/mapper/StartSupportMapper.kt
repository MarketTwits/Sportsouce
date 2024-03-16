package com.markettwits.start_support.data.mapper

import com.markettwits.cloud.model.start_donation.StartDonationRequest

interface StartSupportMapper {
    suspend fun map(startId: Int, price: Int): StartDonationRequest
}
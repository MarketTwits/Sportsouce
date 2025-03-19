package com.markettwits.start_support.data.mapper

import com.markettwits.start_cloud.model.donation.StartDonationRequest

interface StartSupportMapper {
    suspend fun map(startId: Int, price: Int): StartDonationRequest
}
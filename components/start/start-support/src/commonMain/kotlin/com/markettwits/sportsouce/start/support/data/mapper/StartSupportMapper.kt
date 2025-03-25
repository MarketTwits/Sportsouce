package com.markettwits.sportsouce.start.support.data.mapper

import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationRequest

interface StartSupportMapper {
    suspend fun map(startId: Int, price: Int): StartDonationRequest
}
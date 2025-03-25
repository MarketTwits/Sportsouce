package com.markettwits.sportsouce.start.support.data.mapper

import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationRequest

internal class StartSupportMapperBase : StartSupportMapper {
    override suspend fun map(startId: Int, price: Int): StartDonationRequest =
        StartDonationRequest(startId, price)

}

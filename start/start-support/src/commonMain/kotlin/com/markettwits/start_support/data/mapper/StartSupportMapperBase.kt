package com.markettwits.start_support.data.mapper

import com.markettwits.start_cloud.model.donation.StartDonationRequest

internal class StartSupportMapperBase : StartSupportMapper {
    override suspend fun map(startId: Int, price: Int): StartDonationRequest =
        StartDonationRequest(startId, price)

}

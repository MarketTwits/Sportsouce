package com.markettwits.start_support.data

import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_support.data.mapper.StartSupportMapper

internal class StartSupportRepositoryBase(
    private val service: SportSauceStartApi,
    private val mapper: StartSupportMapper,
) : StartSupportRepository {
    override suspend fun donation(startId: Int, price: Int): Result<String> = runCatching {
        service.donation(mapper.map(startId, price)).url
    }

}
package com.markettwits.sportsouce.start.support.data

import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapper

internal class StartSupportRepositoryBase(
    private val service: SportSauceStartApi,
    private val mapper: StartSupportMapper,
) : StartSupportRepository {
    override suspend fun donation(startId: Int, price: Int): Result<String> = runCatching {
        service.donation(mapper.map(startId, price)).url
    }

}
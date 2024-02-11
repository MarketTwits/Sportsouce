package com.markettwits.random.random.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts.data.StartsCloudToUiMapper
import kotlin.random.Random

class RandomRepositoryBase(
    private val service: SportsouceApi,
    private val startsRemoteToUiMapper: StartsCloudToUiMapper
) : RandomRepository {
    override suspend fun randomStart(): Result<Int> {
        val b = runCatching {
            val result = service.startWithFilter(randomRequest())
            startsRemoteToUiMapper.mapSingle(result.rows).map { it.id }.first()
        }
        return b

    }

    private fun randomRequest(): Map<String, String> =
        mapOf("skipCount" to Random.nextInt(from = 1, until = 173).toString())
}
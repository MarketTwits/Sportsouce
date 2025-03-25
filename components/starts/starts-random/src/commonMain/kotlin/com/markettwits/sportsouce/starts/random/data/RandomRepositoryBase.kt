package com.markettwits.sportsouce.starts.random.data

import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import kotlin.random.Random

class RandomRepositoryBase(
    private val service: SportSauceStartsApi,
) : RandomRepository {
    override suspend fun randomStart(): Result<Int> {
        val value = runCatching {
            val result = service.startWithFilter(randomRequest())
            result.map { it.id }.first()
        }
        return value
    }

    private fun randomRequest(): Map<String, String> =
        mapOf("skipCount" to Random.nextInt(from = 1, until = 173).toString())
}
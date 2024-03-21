package com.markettwits.schedule.schedule.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.schedule.schedule.domain.ScheduleRepository
import com.markettwits.schedule.schedule.domain.StartsSchedule
import com.markettwits.starts_common.data.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

internal class ScheduleRepositoryBase(
    private val service: SportsouceApi,
    private val startRemoteToUiMapper: StartsCloudToListMapper,
    private val startScheduleMapper: StartScheduleMapper
) : ScheduleRepository {

    override suspend fun actual(): Result<List<StartsSchedule>> {
        return runCatching {
            val actual = coroutineScope {
                withContext(Dispatchers.IO) {
                    val deferredActual = async { service.fetchActualStarts() }
                    deferredActual.await()
                }
            }
            val data = startRemoteToUiMapper.mapSingle(actual.rows)
            startScheduleMapper.map(data)
        }
    }

    override suspend fun allStarts(): Result<List<StartsListItem>> = runCatching {
        startRemoteToUiMapper.mapSingle(service.fetchActualStarts().rows)
    }
}
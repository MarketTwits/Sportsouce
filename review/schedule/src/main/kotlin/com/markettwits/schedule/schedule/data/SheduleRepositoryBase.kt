package com.markettwits.schedule.schedule.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start.data.StartRemoteToUiMapper
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.presentation.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

internal class ScheduleRepositoryBase(
    private val service: SportsouceApi,
    private val startRemoteToUiMapper: StartsCloudToUiMapper,
    private val startScheduleMapper : StartScheduleMapper
) : ScheduleRepository {

    override suspend fun starts(): Result<List<StartsListItem>> {
        return runCatching {
            val actual  = coroutineScope {
                withContext(Dispatchers.IO){
                    val deferredActual = async { service.fetchActualStarts() }
                    deferredActual.await()
                }
            }
            val data = startRemoteToUiMapper.mapSingle(actual.rows)
            startScheduleMapper.map(data)
        }
    }
}
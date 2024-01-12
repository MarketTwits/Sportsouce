package com.markettwits.schedule.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start.data.StartRemoteToUiMapper

internal class ScheduleRepositoryBase(
    private val service: SportsouceApi,
    private val startRemoteToUiMapper: StartRemoteToUiMapper
) : ScheduleRepository {


}
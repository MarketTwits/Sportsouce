package com.markettwits.schedule.schedule.domain

import com.markettwits.starts_common.domain.StartsListItem

interface ScheduleRepository {
    suspend fun actual(): Result<List<StartsSchedule>>
    suspend fun allStarts(): Result<List<StartsListItem>>
}
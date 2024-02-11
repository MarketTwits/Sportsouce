package com.markettwits.schedule.schedule.data

import com.markettwits.schedule.schedule.domain.StartsSchedule

interface ScheduleRepository {
    suspend fun starts() : Result<List<StartsSchedule>>
}
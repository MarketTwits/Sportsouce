package com.markettwits.schedule.schedule.data

import com.markettwits.starts.presentation.StartsListItem

interface ScheduleRepository {
    suspend fun starts() : Result<List<StartsListItem>>
}
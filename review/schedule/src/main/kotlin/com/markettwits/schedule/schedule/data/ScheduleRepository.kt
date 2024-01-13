package com.markettwits.schedule.schedule.data

import com.markettwits.schedule.schedule.domain.StartsSchedule
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore
import com.markettwits.starts.presentation.StartsListItem

interface ScheduleRepository {
    suspend fun starts() : Result<List<StartsSchedule>>
}
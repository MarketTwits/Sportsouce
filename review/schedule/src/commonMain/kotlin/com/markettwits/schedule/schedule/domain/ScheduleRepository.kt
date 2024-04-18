package com.markettwits.schedule.schedule.domain

import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun schedule(forced: Boolean = false): Flow<List<StartsListItem>>
}
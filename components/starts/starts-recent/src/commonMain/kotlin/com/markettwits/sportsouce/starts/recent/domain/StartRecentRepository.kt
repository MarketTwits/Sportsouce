package com.markettwits.sportsouce.starts.recent.domain

import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.StateFlow

interface StartRecentRepository {

    val recentStarts: StateFlow<List<StartsListItem>>

    suspend fun add(start: StartsListItem)
}

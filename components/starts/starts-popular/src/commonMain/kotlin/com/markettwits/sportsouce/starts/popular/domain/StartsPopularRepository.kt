package com.markettwits.sportsouce.starts.popular.domain

import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

internal interface StartsPopularRepository {
    suspend fun popularStarts(): Flow<List<StartsListItem>>
}
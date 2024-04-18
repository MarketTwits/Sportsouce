package com.markettwits.popular.popular.domain

import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

internal interface StartsPopularRepository {
    suspend fun popularStarts(): Flow<List<StartsListItem>>
}
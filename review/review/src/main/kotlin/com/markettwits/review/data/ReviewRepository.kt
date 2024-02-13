package com.markettwits.review.data

import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow


interface ReviewRepository {
    suspend fun launch(): Flow<Result<List<List<StartsListItem>>>>
}
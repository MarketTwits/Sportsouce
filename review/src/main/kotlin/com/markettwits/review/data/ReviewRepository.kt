package com.markettwits.review.data

import com.markettwits.review.domain.ActualStart
import com.markettwits.starts.StartsListItem

interface ReviewRepository {
    suspend fun launch(): Result<List<List<StartsListItem>>>
}
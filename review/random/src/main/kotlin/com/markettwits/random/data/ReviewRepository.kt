package com.markettwits.random.data

import com.markettwits.random.domain.ActualStart
import com.markettwits.starts.StartsListItem

interface ReviewRepository {
    suspend fun launch(): Result<List<List<StartsListItem>>>
}
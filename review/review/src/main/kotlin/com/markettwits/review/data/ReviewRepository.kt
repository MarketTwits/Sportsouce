package com.markettwits.review.data

import com.markettwits.starts.presentation.StartsListItem

interface ReviewRepository {
    suspend fun launch(): Result<List<List<StartsListItem>>>
}
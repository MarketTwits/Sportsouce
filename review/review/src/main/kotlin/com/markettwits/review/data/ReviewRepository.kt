package com.markettwits.review.data

import com.markettwits.starts_common.domain.StartsListItem


interface ReviewRepository {
    suspend fun launch(): Result<List<List<StartsListItem>>>
}
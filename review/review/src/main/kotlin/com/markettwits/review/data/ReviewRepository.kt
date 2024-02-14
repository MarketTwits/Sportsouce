package com.markettwits.review.data

import com.markettwits.review.domain.Review
import kotlinx.coroutines.flow.Flow


interface ReviewRepository {
    suspend fun launch(forced: Boolean = false): Flow<Result<Review>>
}
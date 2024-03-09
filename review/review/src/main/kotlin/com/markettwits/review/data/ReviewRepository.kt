package com.markettwits.review.data

import com.markettwits.review.domain.Review
import kotlinx.coroutines.flow.Flow


interface ReviewRepository {
    suspend fun review(forced: Boolean = false): Flow<Review>
}
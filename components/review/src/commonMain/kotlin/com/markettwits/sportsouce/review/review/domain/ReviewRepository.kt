package com.markettwits.sportsouce.review.review.domain

import kotlinx.coroutines.flow.Flow


interface ReviewRepository {

    suspend fun review(forced: Boolean = false): Flow<Review>

}
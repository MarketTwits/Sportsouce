package com.markettwits.sportsouce.review.review.domain

import kotlinx.coroutines.flow.Flow

interface ReviewPhotoAlbumsRepository {
    fun albums(): Flow<List<ReviewPhotoAlbum>>
}

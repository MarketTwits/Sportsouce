package com.markettwits.sportsouce.review.review.domain

import kotlinx.serialization.Serializable

@Serializable
data class ReviewPhotoAlbum(
    val id: Int,
    val startName: String,
    val name: String,
    val date: String,
    val photosCount: Int,
    val previewUrl: String,
)

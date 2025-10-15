package com.markettwits.sportsouce.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ClubMainImage(
    val id: Int,
    val name: String,
    val imageUrl: String,
)
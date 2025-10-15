package com.markettwits.sportsouce.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ClubFeature(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
)
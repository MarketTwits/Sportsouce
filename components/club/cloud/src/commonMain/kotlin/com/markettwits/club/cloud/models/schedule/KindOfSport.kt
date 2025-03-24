package com.markettwits.club.cloud.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val id: Int,
    val name: String
)
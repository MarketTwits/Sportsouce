package com.markettwits.cloud_clubs.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val id: Int,
    val name: String
)
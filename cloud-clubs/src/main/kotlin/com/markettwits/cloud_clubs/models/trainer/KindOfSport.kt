package com.markettwits.cloud_clubs.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val TrainerToKindOfSport: TrainerToKindOfSport,
    val id: Int,
    val name: String
)
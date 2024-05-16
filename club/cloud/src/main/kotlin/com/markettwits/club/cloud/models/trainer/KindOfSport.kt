package com.markettwits.club.cloud.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val TrainerToKindOfSport: TrainerToKindOfSport,
    val id: Int,
    val name: String
)
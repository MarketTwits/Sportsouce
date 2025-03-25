package com.markettwits.sportsouce.club.cloud.models.trainer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    @SerialName("TrainerToKindOfSport")
    val trainerToKindOfSport: TrainerToKindOfSport,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
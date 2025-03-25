package com.markettwits.sportsouce.club.cloud.models.trainer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainerToKindOfSport(
    @SerialName("id")
    val id: Int,
    @SerialName("kindOfSport_id")
    val kindofsportId: Int,
    @SerialName("trainer_id")
    val trainerId: Int
)
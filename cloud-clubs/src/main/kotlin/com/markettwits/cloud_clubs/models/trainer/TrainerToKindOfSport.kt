package com.markettwits.cloud_clubs.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class TrainerToKindOfSport(
    val id: Int,
    val kindOfSport_id: Int,
    val trainer_id: Int
)
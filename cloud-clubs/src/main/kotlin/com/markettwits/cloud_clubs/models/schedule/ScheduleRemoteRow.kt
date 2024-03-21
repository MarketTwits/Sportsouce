package com.markettwits.cloud_clubs.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRemoteRow(
    val address: String,
    val createdAt: String,
    val description: String,
    val file_id: Int?,
    val id: Int,
    val kindOfSport: KindOfSport,
    val kindOfSportId: Int,
    val startDate: String,
    val trainer: Trainer,
    val trainer_id: Int,
    val updatedAt: String,
    val weekday: String,
    val workout: Workout,
    val workout_id: Int
)
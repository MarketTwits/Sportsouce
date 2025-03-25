package com.markettwits.sportsouce.club.cloud.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRemoteRow(
    @SerialName("address")
    val address: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("file_id")
    val fileId: Int? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("kindOfSport")
    val kindOfSport: KindOfSport,
    @SerialName("kindOfSportId")
    val kindOfSportId: Int,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("trainers")
    val trainers: List<Trainer>,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("weekday")
    val weekday: String,
    @SerialName("workout")
    val workout: Workout? = null,
    @SerialName("workout_id")
    val workoutId: Int? = null
)
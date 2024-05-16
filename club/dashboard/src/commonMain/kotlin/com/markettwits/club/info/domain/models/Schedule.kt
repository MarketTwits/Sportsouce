package com.markettwits.club.info.domain.models

import com.markettwits.club.cloud.models.schedule.Workout

data class Schedule(
    val id: Int,
    val address: String,
    val description: String,
    val kindOfSport: String,
    val startDate: String,
    val trainerFullName: String,
    val weekday: String,
    val workoutTitle: String
)
package com.markettwits.sportsouce.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: Int,
    val workoutId: Int,
    val address: String,
    val description: String,
    val kindOfSport: String,
    val startDate: String,
    val trainerFullName: String,
    val weekday: String,
    val workoutTitle: String
)
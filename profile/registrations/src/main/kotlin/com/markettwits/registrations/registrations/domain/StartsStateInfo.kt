package com.markettwits.registrations.registrations.domain

import com.markettwits.cloud.model.common.StartStatus
import kotlinx.serialization.Serializable

@Serializable
data class StartsStateInfo(
    val id: Int,
    val startId: Int,
    val name: String,
    val image: String,
    val dateStart: String,
    val statusCode: StartStatus,
    val team: String,
    val payment: Boolean,
    val ageGroup: String,
    val distance: String,
    val member: String,
    val kindOfSport: String,
    val startTitle: String,
    val cost: String,
)
package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    @SerialName("age_from")
    val ageFrom: Int?,
    @SerialName("age_to")
    val ageTo: Int?,
    @SerialName("format")
    val format: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("start_id")
    val startId: Int?
)
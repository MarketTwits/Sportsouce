package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    @SerialName("age_from")
    val ageFrom: Int? = null,
    @SerialName("age_to")
    val ageTo: Int? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("start_id")
    val startId: Int? = null,
)
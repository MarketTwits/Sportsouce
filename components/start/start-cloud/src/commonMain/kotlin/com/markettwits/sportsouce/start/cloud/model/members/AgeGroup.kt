package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    @SerialName("age_from")
    val ageFrom: Int,
    @SerialName("age_to")
    val ageTo: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
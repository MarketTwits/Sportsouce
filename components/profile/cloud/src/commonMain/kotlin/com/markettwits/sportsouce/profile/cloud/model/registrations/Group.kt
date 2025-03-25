package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName("name")
    val name: String,
    @SerialName("sex")
    val sex: String? = null,
    @SerialName("ageFrom")
    val ageFrom: String,
    @SerialName("ageTo")
    val ageTo: String,
    @SerialName("stages")
    val stages: List<Stage>? = null
)
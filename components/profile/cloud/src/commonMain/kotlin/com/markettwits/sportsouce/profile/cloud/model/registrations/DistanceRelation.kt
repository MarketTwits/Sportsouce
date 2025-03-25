package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    @SerialName("format")
    val format: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
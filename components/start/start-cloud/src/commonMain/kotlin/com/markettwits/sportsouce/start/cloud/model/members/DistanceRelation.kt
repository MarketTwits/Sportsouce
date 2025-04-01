package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
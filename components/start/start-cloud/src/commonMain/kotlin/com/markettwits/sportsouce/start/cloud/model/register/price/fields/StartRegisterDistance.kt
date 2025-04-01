package com.markettwits.sportsouce.start.cloud.model.register.price.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterDistance(
    @SerialName("distance_id")
    val distanceId: Int,
    @SerialName("member")
    val member: List<StartRegisterMember>
)
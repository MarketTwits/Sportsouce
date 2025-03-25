package com.markettwits.sportsouce.start.cloud.model.register.price.fields

import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterDistance(
    val distance_id: Int,
    val member: List<StartRegisterMember>
)
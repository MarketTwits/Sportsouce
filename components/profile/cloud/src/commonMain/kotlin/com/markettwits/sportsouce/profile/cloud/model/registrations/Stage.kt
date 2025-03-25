package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    @SerialName("value")
    val value: String,
    @SerialName("sex")
    val sex: List<String>
)
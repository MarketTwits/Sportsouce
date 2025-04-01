package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartStatus(
    @SerialName("code") val code: Int,
    @SerialName("name") val name: String
)
package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
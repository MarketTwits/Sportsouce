package com.markettwits.sportsouce.start.cloud.model.start.fields


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sponsor(
    @SerialName("file")
    val file: File,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("site")
    val site: String = "",
)
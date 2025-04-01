package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Option(
    @SerialName("fieldId")
    val fieldId: Int?,
    @SerialName("id")
    val id: Int,
    @SerialName("price")
    val price: Int?,
    @SerialName("title")
    val title: String,
    @SerialName("uuid")
    val uuid: String? = null
)
package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsefulLinks(
    @SerialName("id")
    val id: Int,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("url")
    val url : String,
    @SerialName("text")
    val text : String
)
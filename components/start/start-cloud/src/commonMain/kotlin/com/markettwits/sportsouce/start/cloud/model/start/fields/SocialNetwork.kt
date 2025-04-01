package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialNetwork(
    @SerialName("code")
    val code: String,
    @SerialName("id")
    val id: Int,
    @SerialName("organizer_id")
    val organizerId: Int?,
    @SerialName("start_id")
    val startId: Int?,
    @SerialName("url")
    val url: String?
)
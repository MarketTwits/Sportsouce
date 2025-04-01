package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Organizer(
    @SerialName("id")
    val id: Int,
    @SerialName("messengers")
    val messengers: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("social_networks")
    val socialNetworks: List<SocialNetwork>,
    @SerialName("start_id")
    val startId: Int
)
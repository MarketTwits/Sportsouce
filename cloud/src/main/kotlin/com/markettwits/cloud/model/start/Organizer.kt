package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class Organizer(
    val id: Int,
    val messengers: String,
    val name: String,
    val phone: String,
    val social_networks: List<SocialNetwork>?,
    val start_id: Int
)
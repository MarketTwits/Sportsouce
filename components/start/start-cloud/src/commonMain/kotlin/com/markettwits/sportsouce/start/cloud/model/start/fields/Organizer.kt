package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Organizer(
    val id: Int,
    val messengers: String,
    val name: String,
    val phone: String?,
    val social_networks: List<SocialNetwork>,
    val start_id: Int
)
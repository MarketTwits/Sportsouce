package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class SocialNetwork(
    val code: String,
    val id: Int,
    val organizer_id: Int?,
    val start_id: Int?,
    val url: String?
)
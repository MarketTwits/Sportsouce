package com.markettwits.sportsouce.edit_profile.edit_social_network.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserSocialNetwork(
    val telegram: String,
    val whatsApp: String,
    val vk: String,
    val instagram: String
)
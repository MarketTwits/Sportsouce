package com.markettwits.club.cloud.models.club_settings

import kotlinx.serialization.Serializable

@Serializable
data class ClubSettingsRemoteRow(
    val createdAt: String,
    val description: String?,
    val id: Int,
    val image: com.markettwits.club.cloud.models.common.File?,
    val imageId: Int?,
    val key: String,
    val name: String,
    val updatedAt: String
)
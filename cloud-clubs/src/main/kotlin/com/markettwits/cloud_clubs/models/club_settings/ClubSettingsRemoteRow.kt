package com.markettwits.cloud_clubs.models.club_settings

import com.markettwits.cloud_clubs.models.common.File
import kotlinx.serialization.Serializable

@Serializable
data class ClubSettingsRemoteRow(
    val createdAt: String,
    val description: String?,
    val id: Int,
    val image: File?,
    val imageId: Int?,
    val key: String,
    val name: String,
    val updatedAt: String
)
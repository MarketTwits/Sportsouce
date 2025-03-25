package com.markettwits.sportsouce.club.cloud.models.club_settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubSettingsRemoteRow(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: com.markettwits.sportsouce.club.cloud.models.common.File?,
    @SerialName("imageId")
    val imageId: Int?,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String
)
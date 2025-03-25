package com.markettwits.sportsouce.club.cloud.models.club_settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ClubSettingsRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<ClubSettingsRemoteRow>
)
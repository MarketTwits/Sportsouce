package com.markettwits.cloud_clubs.models.club_settings

import kotlinx.serialization.Serializable

@Serializable
data class ClubSettingsRemote(
    val count: Int,
    val rows: List<ClubSettingsRemoteRow>
)
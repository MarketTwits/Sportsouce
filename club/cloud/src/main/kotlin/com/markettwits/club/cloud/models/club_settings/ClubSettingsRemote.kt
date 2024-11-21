package com.markettwits.club.cloud.models.club_settings

import kotlinx.serialization.Serializable

@Serializable
internal data class ClubSettingsRemote(
    val count: Int,
    val rows: List<ClubSettingsRemoteRow>
)
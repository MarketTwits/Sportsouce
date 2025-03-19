package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationStart(
    val id: Int,
    val name: String,
    val posterLinkFile: PosterLinkFile?,
    val start_date: String,
    @DeprecatedField
    val start_status: CloudStartStatus? = null,
    @DeprecatedField
    val coordinates: String? = null,
)
package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRegistrationsRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<UserRegistration>
)
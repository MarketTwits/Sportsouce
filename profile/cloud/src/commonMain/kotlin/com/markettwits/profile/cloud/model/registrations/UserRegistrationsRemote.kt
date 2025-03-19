package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
internal data class UserRegistrationsRemote(
    val count: Int,
    val rows: List<UserRegistration>
)
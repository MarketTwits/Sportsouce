package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
internal data class UserRegistrationsRemote(
    val count: Int,
    val rows: List<UserRegistration>
)
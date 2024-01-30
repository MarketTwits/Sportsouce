package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TimezoneRemote(
    val description: String,
    val offset: String
)
package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class StreetRemote(
    val name: String,
    val number: Int
)
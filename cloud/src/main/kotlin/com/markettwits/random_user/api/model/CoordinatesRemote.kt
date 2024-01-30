package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesRemote(
    val latitude: String,
    val longitude: String
)
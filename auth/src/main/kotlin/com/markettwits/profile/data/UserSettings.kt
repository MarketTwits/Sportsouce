package com.plcoding.androidcrypto

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val id : Long,
    val email: String,
    val password: String,
    val accessToken : String,
    val refreshToken : String,
)

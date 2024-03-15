package com.markettwits.profile.internal.database.data.entities

import kotlinx.serialization.Serializable


@Serializable
internal data class CredentialRealmCache(
    val userId: Long,
    val email: String,
    val password: String,
    val accessToken: String,
    val refreshToken: String,
)



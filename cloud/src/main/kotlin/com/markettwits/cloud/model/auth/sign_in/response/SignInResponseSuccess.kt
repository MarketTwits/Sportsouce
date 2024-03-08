package com.markettwits.cloud.model.auth.sign_in.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseSuccess(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
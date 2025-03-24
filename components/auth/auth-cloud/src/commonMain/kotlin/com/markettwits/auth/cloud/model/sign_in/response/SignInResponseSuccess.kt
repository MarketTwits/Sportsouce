package com.markettwits.auth.cloud.model.sign_in.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseSuccess(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
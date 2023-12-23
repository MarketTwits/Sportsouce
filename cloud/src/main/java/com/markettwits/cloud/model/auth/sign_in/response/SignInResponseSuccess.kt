package com.markettwits.cloud.model.auth.sign_in.response

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import kotlinx.serialization.Serializable
@Serializable
data class SignInResponseSuccess(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
@Serializable
data class SignInResponse(
    val success: SignInResponseSuccess? = null,
    val error: AuthErrorResponse? = null,
)
package com.markettwits.auth.cloud.model.sign_in.request

import com.markettwits.auth.cloud.model.sign_in.request.Redirect
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String,
    val kind: String = "login",
    val redirect: Redirect = Redirect(true)
)
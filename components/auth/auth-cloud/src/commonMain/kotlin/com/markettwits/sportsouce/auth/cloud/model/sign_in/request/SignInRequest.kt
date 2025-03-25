package com.markettwits.sportsouce.auth.cloud.model.sign_in.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("kind")
    val kind: String = "login",
    @SerialName("redirect")
    val redirect: Redirect = Redirect(true)
)
package com.markettwits.sportsouce.auth.cloud.model.sign_up

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("birthday")
    val birthday: String,
    @SerialName("email")
    val email: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("name")
    val name: String,
    @SerialName("number")
    val number: String,
    @SerialName("password")
    val password: String,
    @SerialName("sex")
    val sex: String,
    @SerialName("surname")
    val surname: String
)
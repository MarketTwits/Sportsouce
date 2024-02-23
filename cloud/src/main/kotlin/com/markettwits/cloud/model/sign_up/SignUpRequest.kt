package com.markettwits.cloud.model.sign_up

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val birthday: String,
    val email: String,
    val kind: String,
    val name: String,
    val number: String,
    val password: String,
    val sex: String,
    val surname: String
)
package com.markettwits.sportsouce.auth.cloud.model.sign_in.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Redirect(
    @SerialName("isTrusted")
    val isTrusted: Boolean
)
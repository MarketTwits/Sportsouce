package com.markettwits.auth.cloud.model.sign_in.request

import kotlinx.serialization.Serializable

@Serializable
data class Redirect(
    val isTrusted: Boolean
)
package com.markettwits.cloud.model.auth.sign_in.request

import kotlinx.serialization.Serializable

@Serializable
data class Redirect(
    val isTrusted: Boolean
)
package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class IdRemote(
    val name: String,
    val value: String?
)
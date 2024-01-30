package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class DobRemote(
    val age: Int,
    val date: String
)
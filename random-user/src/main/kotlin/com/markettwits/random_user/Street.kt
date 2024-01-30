package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class Street(
    val name: String,
    val number: Int
)
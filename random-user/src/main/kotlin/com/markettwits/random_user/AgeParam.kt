package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class AgeParam(
    val age: Int,
    val date: String
)
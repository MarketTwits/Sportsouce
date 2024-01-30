package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val first: String,
    val last: String,
    val title: String,
    val full : String
)
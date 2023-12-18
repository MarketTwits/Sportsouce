package com.markettwits.cloud.model.start_comments

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val photo: String?,
    val surname: String
)
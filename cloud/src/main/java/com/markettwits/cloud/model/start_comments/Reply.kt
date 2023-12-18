package com.markettwits.cloud.model.start_comments

import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val comment: String,
    val createdAt: String,
    val id: Int,
    val user: User
)
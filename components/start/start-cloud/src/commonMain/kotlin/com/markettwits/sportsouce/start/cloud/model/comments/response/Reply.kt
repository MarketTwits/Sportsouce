package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val comment: String,
    val createdAt: String,
    val id: Int,
    val user: UserComment
)
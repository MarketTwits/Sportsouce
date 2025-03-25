package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val comment: String,
    val countSub: Int,
    val createdAt: String,
    val id: Int,
    val personId: String,
    val replies: List<Reply>? = null,
    val startId: Int,
    val updatedAt: String,
    val user: UserComment
)
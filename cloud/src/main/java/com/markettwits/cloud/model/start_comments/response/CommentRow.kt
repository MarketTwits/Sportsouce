package com.markettwits.cloud.model.start_comments.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentRow(
    val comment: String,
    val countSub: Int,
    val createdAt: String,
    val id: Int,
    val newsId: Int?,
    val personId: String,
    val replies: List<Reply>? = null,
    val startId: Int,
    val updatedAt: String,
    val user: User
)
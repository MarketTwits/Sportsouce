package com.markettwits.cloud.model.start_comments

import kotlinx.serialization.Serializable

@Serializable
data class Row(
    val comment: String,
    val countSub: Int,
    val createdAt: String,
    val id: Int,
    val newsId: Int?,
    val personId: String,
    val replies: List<Reply>,
    val startId: Int,
    val updatedAt: String,
    val user: User
)
package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName("comment")
    val comment: String,
    @SerialName("countSub")
    val countSub: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("personId")
    val personId: String,
    @SerialName("replies")
    val replies: List<Reply>? = null,
    @SerialName("startId")
    val startId: Int,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("user")
    val user: UserComment
)
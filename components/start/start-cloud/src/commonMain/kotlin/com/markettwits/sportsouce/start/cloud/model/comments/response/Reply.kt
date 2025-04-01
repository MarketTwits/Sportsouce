package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    @SerialName("comment")
    val comment: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("user")
    val user: UserComment
)
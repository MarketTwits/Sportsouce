package com.markettwits.start_cloud.model.comments.response

import com.markettwits.start_cloud.model.comments.response.UserComment
import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val comment: String,
    val createdAt: String,
    val id: Int,
    val user: UserComment
)
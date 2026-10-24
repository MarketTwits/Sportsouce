package com.markettwits.sportsouce.start.cloud.model.comments.request

import kotlinx.serialization.Serializable

@Serializable
data class StartSubCommentRequest(
    val comment: String,
    val parentCommentId: String,
    val startId: String,
)

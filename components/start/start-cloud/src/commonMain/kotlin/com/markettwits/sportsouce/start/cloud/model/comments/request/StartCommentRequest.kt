package com.markettwits.sportsouce.start.cloud.model.comments.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartCommentRequest(
    @SerialName("comment")
    val comment: String,
    @SerialName("isReview")
    val isReview: Boolean,
    @SerialName("averageScore")
    val averageScore: Double,
    @SerialName("startId")
    val startId: Int,
    @SerialName("personId")
    val personId: String,
)
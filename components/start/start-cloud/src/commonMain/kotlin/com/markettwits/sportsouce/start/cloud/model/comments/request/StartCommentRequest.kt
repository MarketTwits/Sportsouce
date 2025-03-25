package com.markettwits.sportsouce.start.cloud.model.comments.request

import kotlinx.serialization.Serializable

@Serializable
data class StartCommentRequest(
    val comment : String,
    val startId : Int,
    val personId : String
)
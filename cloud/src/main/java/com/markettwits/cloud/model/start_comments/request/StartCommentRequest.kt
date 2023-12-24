package com.markettwits.cloud.model.start_comments.request

import kotlinx.serialization.Serializable

@Serializable
data class StartCommentRequest(
    val comment : String,
    val startId : Int,
    val personId : String
)
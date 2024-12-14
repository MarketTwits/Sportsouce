package com.markettwits.start_cloud.model.comments.request

import kotlinx.serialization.Serializable

@Serializable
class StartSubCommentRequest(
    val comment : String,
    val parentCommentId : Int,
    val personId : String,
    val startId : Int
)
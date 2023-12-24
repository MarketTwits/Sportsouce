package com.markettwits.cloud.model.start_comments.request

import kotlinx.serialization.Serializable

@Serializable
class StartSubCommentRequest(
    val comment : String,
    val parentCommentId : Int,
    val personId : String
)
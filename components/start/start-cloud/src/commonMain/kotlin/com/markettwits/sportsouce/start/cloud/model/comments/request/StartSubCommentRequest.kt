package com.markettwits.sportsouce.start.cloud.model.comments.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StartSubCommentRequest(
    @SerialName("comment")
    val comment : String,
    @SerialName("parentCommentId")
    val parentCommentId : Int,
    @SerialName("personId")
    val personId : String,
    @SerialName("startId")
    val startId : Int
)
package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StartCommentsRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<Comment>
)
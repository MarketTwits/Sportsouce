package com.markettwits.sportsouce.start.cloud.model.comments.response

import kotlinx.serialization.Serializable

@Serializable
internal data class StartCommentsRemote(
    val count: Int,
    val rows: List<Comment>
)
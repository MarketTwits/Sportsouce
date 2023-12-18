package com.markettwits.cloud.model.start_comments

import kotlinx.serialization.Serializable

@Serializable
data class StartCommentsRemote(
    val count: Int,
    val rows: List<Row>
)
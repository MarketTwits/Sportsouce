package com.markettwits.cloud.model.starts

import kotlinx.serialization.Serializable

@Serializable
data class StartsRemote(
    val count: Int,
    val rows: List<Row>
)
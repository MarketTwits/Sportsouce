package com.markettwits.cloud.model.starts

import com.markettwits.cloud.model.start.StartData
import com.markettwits.sportsourcedemo.all.Row
import kotlinx.serialization.Serializable

@Serializable
data class StartsRemote(
    val count: Int,
    val rows: List<Row>
)
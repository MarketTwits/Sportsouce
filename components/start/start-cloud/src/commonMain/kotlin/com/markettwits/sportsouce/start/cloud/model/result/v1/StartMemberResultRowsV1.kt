package com.markettwits.sportsouce.start.cloud.model.result.v1


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartMemberResultRowsV1(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("rows")
    val rows: List<StartMemberResultV1> = listOf(),
)
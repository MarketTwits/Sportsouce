package com.markettwits.sportsouce.start.cloud.model.result.v2


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartMembersResultRowsV2(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("page")
    val page: Int = 0,
    @SerialName("rows")
    val rows: List<StartMemberResultV2> = listOf(),
    @SerialName("totalPages")
    val totalPages: Int = 0,
)
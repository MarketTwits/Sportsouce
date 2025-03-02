package com.markettwits.start_cloud.model.result


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StartMemberResultRows(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("rows")
    val rows: List<StartMemberResult> = listOf()
)
package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StartMembersRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<StartMember>
)
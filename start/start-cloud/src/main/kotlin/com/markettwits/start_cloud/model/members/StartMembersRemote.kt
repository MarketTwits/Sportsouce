package com.markettwits.start_cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
internal data class StartMembersRemote(
    val count: Int,
    val rows: List<StartMember>
)
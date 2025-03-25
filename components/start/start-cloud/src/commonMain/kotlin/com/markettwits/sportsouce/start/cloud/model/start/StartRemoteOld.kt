package com.markettwits.sportsouce.start.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class StartRemoteOld(
    val start_data : StartData
) : StartRemote


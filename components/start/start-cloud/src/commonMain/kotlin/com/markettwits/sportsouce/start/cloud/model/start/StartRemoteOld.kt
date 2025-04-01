package com.markettwits.sportsouce.start.cloud.model.start

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRemoteOld(
    @SerialName("start_data")
    val startData : StartData
) : StartRemote


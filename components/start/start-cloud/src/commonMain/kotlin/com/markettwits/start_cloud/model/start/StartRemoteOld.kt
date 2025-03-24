package com.markettwits.start_cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class StartRemoteOld(
    val start_data : StartData
) : StartRemote


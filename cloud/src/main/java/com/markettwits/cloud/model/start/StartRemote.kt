package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class StartRemote(
    val start_data: StartData?
)
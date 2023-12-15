package com.markettwits.cloud.model.common

import com.markettwits.cloud.model.common.StartToKindOfSport
import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val StartToKindOfSport: StartToKindOfSport,
    val id: Int,
    val name: String
)
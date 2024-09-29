package com.markettwits.cloud_shop.model.option

import com.markettwits.cloud_shop.model.common.OptionInfo
import kotlinx.serialization.Serializable

@Serializable
data class OptionRemote(
    val count: Int,
    val rows: List<OptionInfo>,
)
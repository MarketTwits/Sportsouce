package com.markettwits.sportsouce.shop.cloud.model.option

import com.markettwits.sportsouce.shop.cloud.model.common.OptionInfo
import kotlinx.serialization.Serializable

@Serializable
internal data class OptionRemote(
    val count: Int,
    val rows: List<OptionInfo>,
)
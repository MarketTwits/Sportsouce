package com.markettwits.sportsouce.shop.cloud.model.renderFilter

import com.markettwits.sportsouce.shop.cloud.model.common.OptionInfo
import kotlinx.serialization.Serializable

@Serializable
data class Filters(
    val count: Int,
    val rows: List<OptionInfo>,
)
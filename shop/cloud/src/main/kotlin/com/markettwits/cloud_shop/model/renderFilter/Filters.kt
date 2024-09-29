package com.markettwits.cloud_shop.model.renderFilter

import com.markettwits.cloud_shop.model.common.OptionInfo
import kotlinx.serialization.Serializable

@Serializable
data class Filters(
    val count: Int,
    val rows: List<OptionInfo>,
)
package com.markettwits.cloud_shop.model.common

import com.markettwits.cloud_shop.model.product.Value
import kotlinx.serialization.Serializable

@Serializable
data class OptionWithInfo(
    val id: String,
    val option: OptionInfo? = null,
    val option_id: String,
    val product_id: String,
    val value: Value,
    val value_id: String
)
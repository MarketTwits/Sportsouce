package com.markettwits.sportsouce.shop.cloud.model.common

import com.markettwits.sportsouce.shop.cloud.model.product.Value
import kotlinx.serialization.Serializable

@Serializable
data class ProductOption(
    val id: String,
    val option: OptionInfo? = null,
    val option_id: String,
    val product_id: String,
    val value: Value,
    val value_id: String,
)
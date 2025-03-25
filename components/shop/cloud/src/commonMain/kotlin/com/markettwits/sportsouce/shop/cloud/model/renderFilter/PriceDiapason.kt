package com.markettwits.sportsouce.shop.cloud.model.renderFilter

import kotlinx.serialization.Serializable

@Serializable
data class PriceDiapason(
    val max: Int?,
    val min: Int?,
)
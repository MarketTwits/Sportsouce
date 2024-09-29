package com.markettwits.cloud_shop.model.renderFilter

import kotlinx.serialization.Serializable

@Serializable
data class PriceDiapason(
    val max: Int?,
    val min: Int?,
)
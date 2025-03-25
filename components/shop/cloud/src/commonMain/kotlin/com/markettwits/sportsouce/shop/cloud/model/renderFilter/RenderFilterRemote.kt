package com.markettwits.sportsouce.shop.cloud.model.renderFilter

import kotlinx.serialization.Serializable

@Serializable
data class RenderFilterRemote(
    val filters: Filters,
    val priceDiapason: PriceDiapason,
)
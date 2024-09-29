package com.markettwits.cloud_shop.model.renderFilter

import kotlinx.serialization.Serializable

@Serializable
data class RenderFilterRemote(
    val filters: Filters,
    val priceDiapason: PriceDiapason,
)
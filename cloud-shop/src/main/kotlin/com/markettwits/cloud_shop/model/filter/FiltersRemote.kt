package com.markettwits.cloud_shop.model.filter

import kotlinx.serialization.Serializable

@Serializable
data class FiltersRemote(
    val count: Int,
    val rows: List<FilterRow>
)
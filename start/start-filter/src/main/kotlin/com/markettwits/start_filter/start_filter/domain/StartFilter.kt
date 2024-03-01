package com.markettwits.start_filter.start_filter.domain

import kotlinx.serialization.Serializable

@Serializable
internal data class StartFilter(
    val kindOfSport: List<String>,
    val startSeason: List<String>,
    val startStatus: List<StartActual>,
    val city: List<String>,
    val fromDate: String
) {
    @Serializable
    internal data class StartActual(
        val title: String,
        val codes: List<Int>,
    )
}

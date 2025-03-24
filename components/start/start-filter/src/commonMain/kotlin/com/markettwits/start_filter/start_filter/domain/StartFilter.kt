package com.markettwits.start_filter.start_filter.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartFilter(
    val kindOfSport: List<String>,
    val startSeason: List<String>,
    val startStatus: List<StartActual>,
    val city: List<String>,
    val sorted: Sorted
) {
    @Serializable
    data class StartActual(
        val title: String,
        val codes: List<Int>,
    )

    @Serializable
    sealed interface Sorted {
        @Serializable
        data object FirstBefore : Sorted

        @Serializable
        data object LastBefore : Sorted

        @Serializable
        data object Popular : Sorted
    }
}

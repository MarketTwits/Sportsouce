package com.markettwits.start_filter.start_filter.domain

internal data class StartFilter(
    val kindOfSport : List<String>,
    val startSeason : List<String>,
    val startStatus : List<StartActual>,
    val city : List<String>,
    val fromDate : String
){
    internal data class StartActual(
        val title : String,
        val codes : List<Int>,
    )
}

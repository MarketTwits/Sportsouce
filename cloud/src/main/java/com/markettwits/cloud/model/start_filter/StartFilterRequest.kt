package com.markettwits.cloud.model.start_filter

import kotlinx.serialization.Serializable

@Serializable
data class StartFilterRequest(
    val maxResultCount : Int = 6,
    val group : Boolean = true,
    val name : String = "",
    val year : String = "",
    val kindOfSports : String= "",
    val city : String,
    val fromDate : String,
    val toDate : String,
    val status : List<Int>
)

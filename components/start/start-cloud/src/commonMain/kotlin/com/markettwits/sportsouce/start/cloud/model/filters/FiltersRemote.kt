package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.Serializable

@Serializable
data class FiltersRemote(
    val cities: List<FilterCity>,
    val distances: List<Distance>,
    val genders: List<Gender>,
    val groups: List<Group>,
    val teams: List<Team>
)
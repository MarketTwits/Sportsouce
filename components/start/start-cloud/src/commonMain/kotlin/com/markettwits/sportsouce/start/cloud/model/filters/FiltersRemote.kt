package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiltersRemote(
    @SerialName("cities")
    val cities: List<FilterCity>,
    @SerialName("distances")
    val distances: List<Distance>,
    @SerialName("genders")
    val genders: List<Gender>,
    @SerialName("groups")
    val groups: List<Group>,
    @SerialName("teams")
    val teams: List<Team>
)
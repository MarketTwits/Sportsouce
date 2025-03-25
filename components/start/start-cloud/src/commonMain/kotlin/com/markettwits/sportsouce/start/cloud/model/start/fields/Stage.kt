package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    val additional_fields: List<AdditionalField>?,
    val distance_id: Int,
    val id: Int,
    val kind_of_sport: KindOfSport? = null,
    val kind_of_sport_id: Int? = null,
    val name: String,
    val sex: String?,
    val stage_length: String?,
    val uuid: String
)
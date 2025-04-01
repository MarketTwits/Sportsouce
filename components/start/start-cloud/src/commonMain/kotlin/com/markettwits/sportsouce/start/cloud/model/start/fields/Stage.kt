package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    @SerialName("additional_fields")
    val additionalFields: List<AdditionalField>?,
    @SerialName("distance_id")
    val distanceId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("kind_of_sport")
    val kindOfSport: KindOfSport? = null,
    @SerialName("kind_of_sport_id")
    val kindOfSportId: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("sex")
    val sex: String?,
    @SerialName("stage_length")
    val stageLength: String?,
    @SerialName("uuid")
    val uuid: String
)
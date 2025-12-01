package com.markettwits.sportsouce.start.cloud.model.result.v2


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartMemberResultV2(
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("checkpointResults")
    val checkpointResults: List<CheckpointResult> = listOf(),
    @SerialName("distance")
    val distance: String? = "",
    @SerialName("gapToLeader")
    val gapToLeader: String? = "",
    @SerialName("gender")
    val gender: String? = "",
    @SerialName("group")
    val group: String? = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String? = "",
    @SerialName("num")
    val num: String? = "",
    @SerialName("result")
    val result: String? = "",
    @SerialName("secondName")
    val secondName: String? = "",
    @SerialName("team")
    val team: String? = "",
    // Computed field for pagination - not from API
    val computedPlace: Int? = null,
)

@Serializable
data class CheckpointResult(
    @SerialName("displayTime")
    val displayTime: String? = "",
    @SerialName("header")
    val header: String? = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("lap")
    val lap: Int? = 0,
    @SerialName("point")
    val point: String? = "",
    @SerialName("raceAnalysisId")
    val raceAnalysisId: Int? = 0,
    @SerialName("splitTime")
    val splitTime: String? = null,
    @SerialName("time")
    val time: String? = "",
)
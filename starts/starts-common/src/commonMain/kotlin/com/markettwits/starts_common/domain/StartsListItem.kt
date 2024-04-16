package com.markettwits.starts_common.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartsListItem(
    val id: Int,
    val name: String,
    val image: String,
    val date: String,
    val statusCode: StatusCode,
    val place: String,
    val onMainPage: Boolean,
    val distance: String,
    val kindOfSports: List<KindOfSport> = emptyList(),
    val views: Int
) {
    @Serializable
    data class StatusCode(val id: Int, val message: String)

    @Serializable
    data class KindOfSport(val id: Int, val name: String)
}
package com.markettwits.sportsouce.starts.common.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartsListItem(
    val id: Int,
    val name: String,
    val image: String,
    val date: String,
    val statusCode: StatusCode,
    val description: String,
    val slug: String,
    val place: String,
    val onMainPage: Boolean,
    val distance: String,
    val kindOfSports: List<KindOfSport> = emptyList(),
    val organizers: List<Organizer> = emptyList(),
    val slots: Slots? = null,
    val views: Int,
) {

    @Serializable
    data class Slots(
        val openSlots: String,
        val totalSlots: String,
        val isAvailable: Boolean,
    )

    @Serializable
    data class StatusCode(val id: Int, val message: String)

    @Serializable
    data class KindOfSport(val id: Int, val name: String)

    @Serializable
    data class Organizer(
        val name: String,
        val phone: String,
        val isMain: Boolean,
        val photo: OrganizerPhoto? = null,
    )

    @Serializable
    data class OrganizerPhoto(
        val fullPath: String,
        val id: Int,
        val name: String,
        val path: String,
        val extension: String,
    )
}

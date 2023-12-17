package com.markettwits.start.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Groups(
    val name: String,
    val sex: List<String>,
    @SerialName("ageFrom")
    val ageFrom: String,
    @SerialName("ageTo")
    val ageTo: String
)

// Создаем data class для дистанции
@kotlinx.serialization.Serializable
data class Distances(
    val description: String,
    val price: String,
    val prices: List<String>,
    val slots: String,
    @SerialName("infinitySlots")
    val infinitySlots: Boolean,
    @SerialName("track_link")
    val trackLink: Map<String, String>,
    @SerialName("track_code")
    val trackCode: String,
    val format: List<String>,
    val groups: List<Group>,
    @SerialName("infinitySlot")
    val infinitySlot: Boolean
)

@kotlinx.serialization.Serializable
data class MainObject(
    val value: String,
    val sub: String,
    val prompt: Boolean,
    val distances: List<Distance>,
    val groups: List<String>,
    val stages: List<String>,
    val format: String
)

@Serializable
data class MainObjectList(
    val list: List<MainObject>
)
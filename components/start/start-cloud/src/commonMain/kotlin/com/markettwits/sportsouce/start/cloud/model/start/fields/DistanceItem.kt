package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = DistanceItem.DistanceItemSerializer::class)
sealed interface DistanceItem {

    @Serializable
    data class DistanceCombo(
        val value: String,
        val distances: List<DistanceInfo>,
        val sale: String? = null,
        val prices: List<Price>? = null,
        val price: Int? = null,
    ) : DistanceItem

    @Serializable
    data class DistanceInfo(
        val value: String,
        val prompt: Boolean,
        val groups: List<Group>,
        val distanceStages: List<Stage>? = null,
        val format: String,
        val distance: Distance,
    ) : DistanceItem

    @Serializable
    data class Price(
        val c_to: String,
        val c_from: String,
        val value: Int
    )

    @Serializable
    data class Group(
        val name: String,
        val sex: String? = null,
        val ageFrom: String,
        val ageTo: String,
        val stages: List<Stage>? = null
    )

    @Serializable
    data class Distance(
        val description: String,
        val price: String,
        val prices: List<Price>,
        val slots: String,
        val infinitySlots: Boolean,
        val infinitySlot: Boolean? = null,
        val track_code: String,
    )

    @Serializable
    data class Stage(val value: String, val sex: List<String>)

    @Serializable
    data class Discount(
        val id: Int,
        val start_id: Int,
        val c_from: Int,
        val c_to: Int,
        val value: Int,
        val percent: Boolean
    )

    object DistanceItemSerializer :
        JsonContentPolymorphicSerializer<DistanceItem>(DistanceItem::class) {
        override fun selectDeserializer(element: JsonElement) = when {
            "distances" in element.jsonObject -> DistanceCombo.serializer()
            else -> DistanceInfo.serializer()
        }
    }
}

fun Json.mapKindOfSportsToDistanceItemListBase(
    kindOfSport: String
): List<DistanceItem> = try {
    this.decodeFromString<List<DistanceItem>>(kindOfSport)
} catch (e: Exception) {
    emptyList()
}
package com.markettwits.start.data.start.test

import com.markettwits.cloud.ext_model.Distance
import kotlinx.serialization.Serializable

@Serializable
data class SelectKindsSportItem(
    val value: String,
    val groups: List<Group> = emptyList(),
    val format: String = "",
    val distance: Distance? = null,
    val type: String? = null,
    val distances: List<DistanceItem>? = null,
    val sale: String? = null,
    //val prices: List<Price>? = null
){
    @kotlinx.serialization.Serializable
    data class Price(
        val c_to: String,
        val c_from: String,
        val value: Int
    )
    @Serializable
    data class Distance(
        val description: String,
        val price: String,
        val prices: List<Price>? = null,
        val slots: String,
        val infinitySlots: Boolean,
    )

    @Serializable
    data class Group(
        val name: String,
        val ageFrom: String,
        val ageTo: String,
     //   val sex: String
    )

    @Serializable
    data class DistanceItem(
        val value: String,
        val prompt: Boolean = false,
        val groups: List<Group> = emptyList(),
        val format: String = "",
        val distanceStages: List<String> = emptyList(),
        val distance: Distance = Distance("", "", emptyList(), "", false)
    )

    @Serializable
    data class DistanceCombo(
        val value: String,
        val type: String? = null,
        val distances: List<DistanceItem>? = null,
        val sale: String? = null,
        val prices: List<String>? = null
    )
}
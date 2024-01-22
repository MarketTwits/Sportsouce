package com.markettwits.cloud.ext_model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Price(
    val c_to: String,
    val c_from: String,
    val value: Int
)

@kotlinx.serialization.Serializable
data class Group(
    val name: String,
  //  val sex: String,
    val ageFrom: String,
    val ageTo: String
)

@kotlinx.serialization.Serializable
data class Distance(
    val description: String,
    val price: String,
    val prices: List<Price>,
    val slots: String,
    val infinitySlots: Boolean,
    val track_link: Map<String, String>,
    val track_code: String,
)

@Serializable
data class DistanceInfo(
    val value: String,
    val prompt: Boolean,
    val groups: List<Group>,
    val format: String,
    val distance: Distance,
)

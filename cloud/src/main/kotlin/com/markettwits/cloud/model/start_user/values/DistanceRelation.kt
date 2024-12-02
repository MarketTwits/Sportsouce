package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    val format: String,
    val id: Int,
   // val infinite_slots: Boolean?,
    val name: String,
   // val open_slots: Int?,
  //  val slots: Int,
 //   val taken_slots: Int
)
package com.markettwits.start_cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
data class StartMember(
    val createdAt: String,
    val distance_id: Int?,
    val id: Int,
    val is_refunded: Boolean,
    val member_start_group_id: Int,
    val members: List<Member>,
    val payment: Int,
    val reg_code: String,
    val registration_date: String,
    val start_id: Int,
    val updatedAt: String,
    val user_id: Int
)
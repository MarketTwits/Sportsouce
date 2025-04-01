package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartMember(
    @SerialName("id")
    val id: Int,
    @SerialName("distance_id")
    val distanceId: Int?,
    @SerialName("is_refunded")
    val isRefunded: Boolean,
    @SerialName("member_start_group_id")
    val memberStartGroupId: Int,
    @SerialName("members")
    val members: List<Member>,
    @SerialName("payment")
    val payment: Int,
    @SerialName("reg_code")
    val regCode: String,
    @SerialName("registration_date")
    val registrationDate: String,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("user_id")
    val userId: Int
)
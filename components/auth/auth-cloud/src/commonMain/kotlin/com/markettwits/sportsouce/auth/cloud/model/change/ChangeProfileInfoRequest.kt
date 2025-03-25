package com.markettwits.sportsouce.auth.cloud.model.change

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoRequest(
    @SerialName("id")
    val id: Int,
    @SerialName("address")
    val address: String,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("comment_for_address")
    val commentForAddress: String?,
    @SerialName("email")
    val email: String,
    @SerialName("favor")
    val favor: String?,
    @SerialName("instagram")
    val instagram: String?,
    @SerialName("name")
    val name: String,
    @SerialName("number")
    val number: String,
    @SerialName("photo_id")
    val photoId: Int?,
    @SerialName("sex")
    val sex: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String?,
    @SerialName("telegram")
    val telegram: String?,
    @SerialName("vk")
    val vk: String?,
    @SerialName("whatsapp")
    val whatsapp: String?
)


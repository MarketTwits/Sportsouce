package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class StartMemberItem(
    @SerialName("city")
    val city: String?,
    @SerialName("contactPerson")
    val contactPerson: Boolean?,
    @SerialName("day")
    val day: Int,
    @SerialName("distance")
    val distance: String,
    @SerialName("email")
    val email: String? = "",
    @SerialName("format")
    val format: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("group")
    val group: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("instagram")
    val instagram: String? = "",
    @SerialName("name")
    val name: String,
    @SerialName("order_number")
    val orderNumber: String?,
    @SerialName("payment")
    val payment: Int?,
    @SerialName("phone")
    val phone: String? = "",
    @SerialName("price")
    val price: Int? = 0,
    @SerialName("reg_code")
    val regCode: String?,
    @SerialName("sberbank_id")
    val sberbankId: String?,
    @SerialName("start_id")
    val startId: Int?,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String,
    @SerialName("teamNumber")
    val teamNumber: Int? = 0,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("user_id")
    val userId: Int?,
) {
    @Serializable
    data class Group(
        @SerialName("name") val name: String,
    )


    fun mapStartMember(text: String): Group {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return json.decodeFromString<Group>(text)
    }

}
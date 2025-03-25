package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class StartMemberItem(
    val city: String?,
    val contactPerson: Boolean?,
    val day: Int,
    val distance: String,
    val email: String? = "",
    val format: String,
    val gender: String,
    val group: String?,
    val id: Int,
    val instagram: String? = "",
    val name: String,
    val order_number: String?,
    val payment: Int?,
    val phone: String? = "",
    val price: Int? = 0,
    val reg_code: String?,
    val sberbank_id: String?,
    val start_id: Int?,
    val surname: String,
    val team: String,
    val teamNumber: Int? = 0,
    val updatedAt: String,
    val user_id: Int?,
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
package com.markettwits.cloud.model.start_member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.security.acl.Group

@Serializable
data class StartMemberItem(
    val city: String,
    val contactPerson: Boolean,
    val day: Int,
    val distance: String,
    val email: String,
    val format: String,
    val gender: String,
    val group: String,
    val id: Int,
    val instagram: String,
    val name: String,
    val order_number: String?,
    val payment: Int?,
    val phone: String,
    val price: Int,
    val reg_code: String,
    val sberbank_id: String?,
    val start_id: Int?,
    val surname: String,
    val team: String,
    val teamNumber: Int,
    val updatedAt: String,
    val user_id: Int?,
){
    @Serializable
    data class Group(
        @SerialName("name") val name: String,
        @SerialName("sex") val sex: String,
        @SerialName("ageFrom") val ageFrom: String,
        @SerialName("ageTo") val ageTo: String
    )
    fun mapStartMember(text: String) : StartMemberItem.Group{
        val group = Json.decodeFromString<StartMemberItem.Group>(text)
        return group
    }
}
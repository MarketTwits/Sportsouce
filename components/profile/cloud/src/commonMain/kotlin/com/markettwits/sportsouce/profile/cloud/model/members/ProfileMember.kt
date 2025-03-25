package com.markettwits.sportsouce.profile.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileMembers(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<ProfileMember>
) {
    @Serializable
    data class ProfileMember(
        @SerialName("birthday")
        val birthday: String,
        @SerialName("child")
        val child: Boolean?,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("email")
        val email: String,
        @SerialName("gender")
        val gender: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("surname")
        val surname: String,
        @SerialName("team")
        val team: String,
        @SerialName("type")
        val type: String? = null,
        @SerialName("updatedAt")
        val updatedAt: String,
        @SerialName("user_id")
        val userId: Int,
    )
}
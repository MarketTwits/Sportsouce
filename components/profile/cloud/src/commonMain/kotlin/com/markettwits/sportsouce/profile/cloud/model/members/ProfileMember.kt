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
        val birthday: String? = null,
        @SerialName("child")
        val child: Boolean?,
        @SerialName("email")
        val email: String? = null,
        @SerialName("gender")
        val gender: String? = null,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String? = null,
        @SerialName("phone")
        val phone: String? = null,
        @SerialName("surname")
        val surname: String? = null,
        @SerialName("team")
        val team: String? = null,
        @SerialName("type")
        val type: String? = null,
        @SerialName("user_id")
        val userId: Int,
    )
}
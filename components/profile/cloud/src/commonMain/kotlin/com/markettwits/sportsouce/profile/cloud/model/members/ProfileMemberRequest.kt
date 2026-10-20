package com.markettwits.sportsouce.profile.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Owner belongs to creation only. Updating uses the id in the URL. */

@Serializable
data class ProfileMemberRequest(
    @SerialName("birthday")
    val birthday: String,
    @SerialName("child")
    val child: Boolean,
    @SerialName("email")
    val email: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String,
    @SerialName("type")
    val type: String,
    @SerialName("user_id")
    val userId: String? = null
)
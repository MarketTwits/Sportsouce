package com.markettwits.sportsouce.profile.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileMembers(val count: Int, val rows: List<ProfileMember>) {
    /** List and detail share these fields; detail intentionally has no owner user_id. */
    @Serializable
    data class ProfileMember(
        val id: Int,
        val birthday: String? = null,
        val child: Boolean? = null,
        val email: String? = null,
        val gender: String? = null,
        val name: String? = null,
        val phone: String? = null,
        val surname: String? = null,
        val team: String? = null,
        val type: String? = null,
        @SerialName("user_id") val userId: Int? = null,
        @SerialName("participant_user_id") val participantUserId: Int? = null,
        @SerialName("city_id") val cityId: Int? = null,
        val vk: String? = null,
        val instagram: String? = null,
        val whatsapp: String? = null,
        val telegram: String? = null,
    )
}

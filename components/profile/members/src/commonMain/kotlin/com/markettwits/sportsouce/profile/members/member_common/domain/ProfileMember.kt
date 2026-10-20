package com.markettwits.sportsouce.profile.members.member_common.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProfileMember(
    val id: Int,
    val userId: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val gender: String,
    val team: String,
    val birthday: String,
    val type: String,
    val child: Boolean,
    val identityKnown: Boolean = false,
    val participantUserId: Int? = null,
    val cityId: Int? = null,
    val vk: String? = null,
    val instagram: String? = null,
    val whatsapp: String? = null,
    val telegram: String? = null,
    val city: String = "",
    val patronymic: String? = null,
    val source: Source = Source.Saved,
) {
    @Serializable
    enum class Source { Saved, Self, Team }
}

val emptyProfileMember = ProfileMember(
    id = 0,
    userId = 0,
    name = "",
    surname = "",
    email = "",
    phone = "",
    gender = "",
    team = "",
    birthday = "",
    type = "",
    child = false
)

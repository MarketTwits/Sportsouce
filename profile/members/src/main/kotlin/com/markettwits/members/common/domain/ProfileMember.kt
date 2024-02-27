package com.markettwits.members.common.domain

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
    val child: Boolean
)

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
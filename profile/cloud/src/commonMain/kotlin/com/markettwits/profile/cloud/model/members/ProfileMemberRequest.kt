package com.markettwits.profile.cloud.model.members

import kotlinx.serialization.Serializable

/** @see id in post request should be null */

@Serializable
data class ProfileMemberRequest(
    val birthday: String,
    val child: Boolean,
    val email: String,
    val gender: String,
    val id: Int? = null,
    val name: String,
    val phone: String,
    val surname: String,
    val team: String,
    val type: String,
    val user_id: String
)
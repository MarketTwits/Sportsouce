package com.markettwits.cloud.model.profile.members

import kotlinx.serialization.Serializable

@Serializable
data class ProfileMembers(
    val count: Int,
    val rows: List<ProfileMember>
) {
    @Serializable
    data class ProfileMember(
        val birthday: String,
        val child: Boolean,
        val createdAt: String,
        val email: String,
        val gender: String,
        val id: Int,
        val name: String,
        val phone: String,
        val surname: String,
        val team: String,
        val type: String,
        val updatedAt: String,
        val user_id: Int,
    )
}
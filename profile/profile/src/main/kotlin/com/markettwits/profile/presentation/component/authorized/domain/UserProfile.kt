package com.markettwits.profile.presentation.component.authorized.domain

import com.markettwits.cloud.model.start.SocialNetwork
import com.markettwits.registrations.registrations.domain.StartsStateInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val socialNetwork: SocialNetwork,
    val activity: Activity,
    val userInfo: UserInfo
) {
    @Serializable
    data class UserInfo(
        val name: String,
        val surname: String,
        val createdAt: String,
        val status: String,
        val photo: String,
    ) {
        fun fullName() = "$surname $name"
    }

    @Serializable
    data class SocialNetwork(
        val instagram: String,
        val telegram: String,
        val whatsapp: String,
        val vk: String
    )

    @Serializable
    data class Activity(
        val userRegistry: List<StartsStateInfo>,
        val userMemberCount: Int
    )
}

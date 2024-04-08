package com.markettwits.profile.authorized.domain

import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
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
        val userRegistry: List<StartOrderInfo>,
        val userMemberCount: Int
    )
}

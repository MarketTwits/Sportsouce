package com.markettwits.profile.presentation.component.authorized.domain

import com.markettwits.cloud.model.start.SocialNetwork
import com.markettwits.registrations.registrations.domain.StartsStateInfo

data class UserProfile(
    val id: Int,
    val socialNetwork: SocialNetwork,
    val activity: Activity,
    val userInfo: UserInfo
) {
    data class UserInfo(
        val name: String,
        val surname: String,
        val createdAt: String,
        val status: String,
        val photo: String,
    ) {
        fun fullName() = "$surname $name"
    }

    data class SocialNetwork(
        val instagram: String,
        val telegram: String,
        val whatsapp: String,
        val vk: String
    )

    data class Activity(
        val userRegistry: List<StartsStateInfo>,
        val userMemberCount: Int
    )
}

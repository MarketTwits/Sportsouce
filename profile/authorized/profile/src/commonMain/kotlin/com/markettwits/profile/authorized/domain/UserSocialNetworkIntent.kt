package com.markettwits.profile.authorized.domain

sealed interface UserSocialNetworkIntent {
    val value: String

    data class Link(override val value: String) : UserSocialNetworkIntent
    data class Phone(override val value: String) : UserSocialNetworkIntent
}
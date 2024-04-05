package com.markettwits.edit_profile.edit_social_network.domain.handle

import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

class ProfileSocialNetworkHandlerBase : ProfileSocialNetworkHandler {
    override fun handle(userSocialNetwork: UserSocialNetwork): Result<UserSocialNetwork> =
        runCatching {
            userSocialNetwork.copy(
                telegram = addUrlIfNotEmpty("https://t.me/", userSocialNetwork.telegram),
                whatsApp = validateRussianPhoneNumber(userSocialNetwork.whatsApp),
                vk = addUrlIfNotEmpty("https://vk.com/", userSocialNetwork.vk),
                instagram = addUrlIfNotEmpty("https://instagram.com/", userSocialNetwork.instagram)
            )
        }
}

private fun addUrlIfNotEmpty(baseUrl: String, value: String): String {
    return if (value.isNotEmpty()) {
        if (value.startsWith("http://") || value.startsWith("https://")) {
            value
        } else {
            "$baseUrl$value"
        }
    } else {
        ""
    }
}

private fun validateRussianPhoneNumber(phoneNumber: String): String {
    if (phoneNumber.isNotEmpty()) {
        val russianPhoneNumberRegex = Regex("^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}\$")
        if (!russianPhoneNumberRegex.matches(phoneNumber)) {
            throw IllegalArgumentException("Введите корректный номер телефона в формате:\n +7 (000) 000-00-00, либо удалите его")
        }
    }
    return phoneNumber
}

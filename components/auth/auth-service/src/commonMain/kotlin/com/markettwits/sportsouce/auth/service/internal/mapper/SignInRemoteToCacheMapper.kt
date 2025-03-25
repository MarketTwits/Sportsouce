package com.markettwits.sportsouce.auth.service.internal.mapper

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.SignInResponseSuccess
import com.markettwits.sportsouce.auth.service.internal.database.data.entities.CredentialRealmCache

internal class SignInRemoteToCacheMapper {

    fun map(remote: SignInResponseSuccess, password: String): CredentialRealmCache =
        CredentialRealmCache(
            userId = remote.user.id.toLong(),
            email = remote.user.email ?: "",
            password = password,
            accessToken = remote.accessToken,
            refreshToken = remote.refreshToken,
        )
}
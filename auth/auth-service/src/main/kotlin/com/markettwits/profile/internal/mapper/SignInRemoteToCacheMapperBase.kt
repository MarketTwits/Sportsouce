package com.markettwits.profile.internal.mapper

import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache

internal class SignInRemoteToCacheMapperBase : SignInRemoteToCacheMapper {
    override fun map(remote: SignInResponseSuccess, password: String): CredentialRealmCache =
        CredentialRealmCache(
            userId = remote.user.id.toLong(),
            email = remote.user.email,
            password = password,
            accessToken = remote.accessToken,
            refreshToken = remote.refreshToken,
        )
//        CredentialRealmCache().apply {
//            _userId = remote.user.id.toLong()
//            _email = remote.user.email
//            _password = password
//            _accessToken = remote.accessToken
//            _refreshToken = remote.refreshToken
//        }
}
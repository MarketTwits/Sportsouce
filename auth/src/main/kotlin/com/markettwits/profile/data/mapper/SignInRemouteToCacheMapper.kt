package com.markettwits.profile.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.profile.data.database.data.entities.CredentialRealmCache

interface SignInRemoteToCacheMapper {
    fun map(remote: SignInResponseSuccess, password: String): CredentialRealmCache

    class Base : SignInRemoteToCacheMapper {
        override fun map(remote: SignInResponseSuccess, password: String): CredentialRealmCache =
            CredentialRealmCache().apply {
                _userId = remote.user.id.toLong()
            _email = remote.user.email
            _password = password
            _accessToken = remote.accessToken
            _refreshToken = remote.refreshToken
        }
    }
}
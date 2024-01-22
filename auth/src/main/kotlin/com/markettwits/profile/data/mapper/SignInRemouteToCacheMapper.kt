package com.markettwits.profile.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.profile.data.database.data.entities.UserSettingsRealmCache

interface SignInRemoteToCacheMapper {
    fun map(remote : SignInResponseSuccess, password: String) : UserSettingsRealmCache

    class Base : SignInRemoteToCacheMapper {
        override fun map(remote: SignInResponseSuccess,password: String): UserSettingsRealmCache = UserSettingsRealmCache().apply {
            _userId = remote.user.id
            _email = remote.user.email
            _password = password
            _accessToken = remote.accessToken
            _refreshToken = remote.refreshToken
        }
    }
}
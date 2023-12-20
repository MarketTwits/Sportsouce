package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.data.store.AuthCacheDataSource
import com.markettwits.profile.data.database.data.entities.UserSettingsRealmCache
import ru.alexpanov.core_network.api.SportsouceApi

class BaseAuthDataSource(
    private val remoteService: SportsouceApi,
    private val local: AuthCacheDataSource
) : AuthDataSource {
    override suspend fun auth(email: String, password: String) {
       // val response = runCatching {
        val response = remoteService.signIn(SignInRequest(email = email, password = password))
      //  }
            local.write(
                UserSettingsRealmCache().apply {
                    _email = response.user.email
                    _password = password
                    _accessToken = response.accessToken
                    _refreshToken = response.refreshToken
                }
            )


    }
    override suspend fun show(): String = local.read()._accessToken

}
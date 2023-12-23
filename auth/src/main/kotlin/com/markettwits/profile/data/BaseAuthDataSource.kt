package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.data.database.data.entities.UserSettingsRealmCache
import com.markettwits.profile.presentation.sign_in.SignInUiState
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import ru.alexpanov.core_network.api.SportsouceApi

class BaseAuthDataSource(
    private val remoteService: SportsouceApi,
    private val signInMapper: SignInRemoteToUiMapper,
    private val signInCacheMapper : SignInRemoteToCacheMapper,
    private val local: AuthCacheDataSource
) : AuthDataSource {
    override suspend fun auth(email: String, password: String): SignInUiState {
        return try {
            val response = remoteService.signIn(SignInRequest(email = email, password = password))
            local.write(signInCacheMapper.map(response, password))
            signInMapper.map(response)
        } catch (e: Exception) {
            when (e) {
                is ClientRequestException -> signInMapper.map(e.response.body<AuthErrorResponse>().message)
                else -> signInMapper.map(e)
            }
        }
    }

    override suspend fun auth(): User {
        return try {
            remoteService.auth(currentToken())
        }catch (e : Exception){
            throw RuntimeException("can't auth #authInner ${e.message}")
        }
    }

    override suspend fun authInner() {
        try {
            val data =  local.read()
            val response = remoteService.signIn(SignInRequest(email = data._email, password = data._password))
             local.write(signInCacheMapper.map(response, data._password))
            signInMapper.map(response)
        }catch (e : Exception){
            throw RuntimeException("can't call #authInner ${e.message}")
        }
    }

    override suspend fun show(): String = local.read()._accessToken
    override suspend fun clear() {
        local.clearAll()
    }

    override suspend fun currentToken(): String {
        return local.read()._accessToken
    }

}
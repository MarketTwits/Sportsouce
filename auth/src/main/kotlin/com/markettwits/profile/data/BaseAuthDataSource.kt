package com.markettwits.profile.data

import android.util.Log
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.presentation.sign_in.SignInUiState
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import com.markettwits.cloud.api.SportsouceApi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class BaseAuthDataSource(
    private val remoteService: SportsouceApi,
    private val signInMapper: SignInRemoteToUiMapper,
    private val signInCacheMapper: SignInRemoteToCacheMapper,
    private val local: AuthCacheDataSource
) : AuthDataSource {
    override suspend fun logIn(email: String, password: String): SignInUiState {
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

    override suspend fun updatePassword(password: String) {
        local.updatePassword(password)
    }

    override suspend fun auth(): User {
        return try {
            remoteService.auth(currentToken())
        } catch (e: Exception) {
            when (e) {
                is ClientRequestException -> signInMapper.map(e.response.body<AuthErrorResponse>().message)
                else -> signInMapper.map(e)
            }
            throw RuntimeException(e.message)
        }
    }

    override suspend fun updateToken(): String {
        return try {
            validateToken()
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun validateToken(): String {
        val data = local.read()
        val token = TokenManager.Base().decode(data._accessToken)
        val time = System.currentTimeMillis() / 1000
        return if (token.expiration > time) {
            data._accessToken
        } else {
            val response =
                remoteService.signIn(SignInRequest(email = data._email, password = data._password))
            local.write(signInCacheMapper.map(response, data._password))
            response.accessToken
        }
    }


    override suspend fun show(): String = local.read()._accessToken
    override suspend fun clear() {
        local.clearAll()
    }

    override suspend fun currentToken(): String {
        return try {
            local.read()._accessToken
        } catch (e: Exception) {
            throw AuthException("Для продолжения аворизуйтесь в приложении")
        }
    }

}
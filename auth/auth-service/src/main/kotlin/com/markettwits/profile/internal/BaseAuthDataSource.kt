package com.markettwits.profile.internal

import com.markettwits.cahce.ObservableCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.internal.database.data.store.AuthCacheDataSource
import com.markettwits.profile.internal.manager.TokenManager
import com.markettwits.profile.internal.mapper.SignInRemoteToCacheMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class BaseAuthDataSource(
    private val remoteService: SportsouceApi,
    private val signInCacheMapper: SignInRemoteToCacheMapper,
    private val cache: ObservableCache<User>,
    private val tokenManager: TokenManager,
    private val local: AuthCacheDataSource
) : AuthDataSource {
    override suspend fun register(signUpRequest: SignUpRequest): Result<Unit> =
        runCatching {
            remoteService.register(signUpRequest)
        }.fold(
            onSuccess = {
                logIn(signUpRequest.email, signUpRequest.password)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun logIn(email: String, password: String): Result<Unit> {
        return runCatching {
            val response = remoteService.signIn(SignInRequest(email = email, password = password))
            local.write(signInCacheMapper.map(response, password))
            auth()
        }
    }

    override suspend fun updatePassword(password: String) {
        local.updatePassword(password)
    }

    override suspend fun auth(): Result<User> =
        updateToken().fold(onSuccess = {
            return runCatching {
                val user = remoteService.auth(it)
                cache.set(value = user)
                user
            }
        }, onFailure = {
            return Result.failure(it)
        })

    override suspend fun updateToken(): Result<String> = validateToken()

    override suspend fun observeUser(): Flow<User> = cache.observe().mapNotNull { it }

    override suspend fun user(): Result<User> = runCatching {
        cache.get() ?: auth().getOrThrow()
    }

    override suspend fun updateUser(request: ChangeProfileInfoRequest): Result<Unit> = runCatching {
        updateToken().fold(onSuccess = {
            remoteService.changeProfileInfo(request, it)
            auth()
        }) {
            Result.failure<Unit>(it)
        }
    }

    private suspend fun validateToken(): Result<String> {
        return runCatching {
            val data = local.read()
            val token = tokenManager.decode(data.accessToken)
            val currentToken = if (tokenManager.isExpired(token.exp)) {
                data.accessToken
            } else {
                val response =
                    remoteService.signIn(
                        SignInRequest(
                            email = data.email,
                            password = data.password
                        )
                    )
                local.write(signInCacheMapper.map(response, data.password))
                response.accessToken
            }
            currentToken
        }
    }

    override suspend fun clear() {
        local.clearAll()
        cache.clear()
    }

}
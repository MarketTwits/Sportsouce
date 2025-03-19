package com.markettwits.profile.internal

import com.markettwits.auth.cloud.SportSauceNetworkAuthApi
import com.markettwits.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.auth.cloud.model.sign_in.request.SignInRequest
import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.auth.cloud.model.sign_up.SignUpRequest
import com.markettwits.cahce.ObservableCache
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.api.SharedUser
import com.markettwits.profile.api.mapToShared
import com.markettwits.profile.internal.database.data.store.AuthCacheDataSource
import com.markettwits.profile.internal.manager.TokenManager
import com.markettwits.profile.internal.mapper.SignInRemoteToCacheMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class BaseAuthDataSource(
    private val remoteService: SportSauceNetworkAuthApi,
    private val signInCacheMapper: SignInRemoteToCacheMapper,
    private val userInfoCache: ObservableCache<User>,
    private val tokenManager: TokenManager,
    private val authCache: AuthCacheDataSource
) : AuthDataSource {

    override suspend fun register(signUpRequest: SignUpRequest): Result<Unit> =
        runCatching {
            remoteService.register(signUpRequest)
        }.fold(
            onSuccess = {
                logIn(signUpRequest.email, signUpRequest.password)
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun logIn(emailOrPhone: String, password: String): Result<User> = runCatching {
        val response =
            remoteService.signIn(SignInRequest(email = emailOrPhone, password = password))
        authCache.write(signInCacheMapper.map(response, password))
        auth().getOrThrow()
    }

    override suspend fun updatePassword(password: String) {
        authCache.updatePassword(password)
    }

    override suspend fun auth(): Result<User> =
        updateToken().fold(onSuccess = {
            return runCatching {
                val user = remoteService.auth(it)
                userInfoCache.set(value = user)
                user
            }
        }, onFailure = {
            return Result.failure(it)
        })

    override suspend fun updateToken(): Result<String> = validateToken()

    override suspend fun observeUser(): Flow<User> = userInfoCache.observe().mapNotNull { it }

    override suspend fun user(): Result<User> = runCatching {
        userInfoCache.get() ?: auth().getOrThrow()
    }

    override suspend fun sharedUser(): Result<SharedUser> = user().mapCatching {
        it.mapToShared()
    }

    override suspend fun userID(): Result<Int> = runCatching {
        user().getOrThrow().id
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
            val data = authCache.read()
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
                authCache.write(signInCacheMapper.map(response, data.password))
                response.accessToken
            }
            currentToken
        }
    }

    override suspend fun clear() {
        authCache.clearAll()
        userInfoCache.clear()
    }

}
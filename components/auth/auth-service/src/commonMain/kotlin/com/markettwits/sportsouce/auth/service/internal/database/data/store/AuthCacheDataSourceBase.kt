package com.markettwits.sportsouce.auth.service.internal.database.data.store


import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.auth.service.internal.database.data.entities.CredentialRealmCache

internal class AuthCacheDataSourceBase(
    private val userCache: UserCredentialCache,
) : AuthCacheDataSource {

    override suspend fun write(data: CredentialRealmCache) {
        userCache.set(value = data)
    }

    override suspend fun updatePassword(password: String) {
        userCache.get()?.copy(password = password)?.let { userCache.set(value = it) }
    }

    override suspend fun read(): CredentialRealmCache =
        current() ?: throw AuthException("Для продолжения авторизуйтесь в приложении")

    override suspend fun current() = userCache.get()

    override fun observe() = userCache.observe()

    override suspend fun clearAll() {
        userCache.clear()
    }
}

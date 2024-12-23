package com.markettwits.profile.internal.database.data.store


import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache

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
        userCache.get() ?: throw AuthException("Для продолжения авторизуйтесь в приложении")


    override suspend fun clearAll() {
        userCache.clear()
    }
}
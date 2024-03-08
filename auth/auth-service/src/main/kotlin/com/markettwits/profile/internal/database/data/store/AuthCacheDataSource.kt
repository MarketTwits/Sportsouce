package com.markettwits.profile.internal.database.data.store

import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache

internal interface AuthCacheDataSource {
    suspend fun write(data: CredentialRealmCache)
    fun updatePassword(password: String)

    fun read(): CredentialRealmCache

    fun clearAll()
}
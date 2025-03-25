package com.markettwits.sportsouce.auth.service.internal.database.data.store

import com.markettwits.sportsouce.auth.service.internal.database.data.entities.CredentialRealmCache

internal interface AuthCacheDataSource {
    suspend fun write(data: CredentialRealmCache)
    suspend fun updatePassword(password: String)
    suspend fun read(): CredentialRealmCache
    suspend fun clearAll()
}
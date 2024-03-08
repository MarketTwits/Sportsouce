package com.markettwits.profile.internal.database.data.store

import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.internal.database.core.RealmDatabaseProvider
import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache
import io.realm.kotlin.ext.query

internal class AuthCacheDataSourceBase(
    private val realmProvider: RealmDatabaseProvider,
) : AuthCacheDataSource {
    private val realm = realmProvider.realm(setOf(CredentialRealmCache::class))

    override suspend fun write(data: CredentialRealmCache) {
        realm.write {
            copyToRealm(data)
        }
    }

    override fun updatePassword(password: String) {
        realm.writeBlocking {
            findLatest(read())?._password = password
        }
    }

    override fun read(): CredentialRealmCache {
        val result = realm.query(CredentialRealmCache::class).find()
        if (result.isEmpty())
            throw AuthException("Для продолжения авторизуйтесь в приложении")
        else
            return result.last()
    }

    override fun clearAll() {
        realm.writeBlocking {
            val writeTransactionItems = query<CredentialRealmCache>().find()
            delete(writeTransactionItems)
        }
    }
}
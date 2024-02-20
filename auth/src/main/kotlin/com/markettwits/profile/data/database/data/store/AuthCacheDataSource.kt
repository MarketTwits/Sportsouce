package com.markettwits.profile.data.database.data.store

import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.entities.CredentialRealmCache
import io.realm.kotlin.ext.query

class AuthCacheDataSource(
    private val realmProvider: RealmDatabaseProvider,
) {
    private val realm = realmProvider.realm(setOf(CredentialRealmCache::class))
    fun write(data: CredentialRealmCache) {
        realm.writeBlocking {
            copyToRealm(data)
        }
    }

    fun updatePassword(password: String) {
        realm.writeBlocking {
            findLatest(read())?._password = password
        }
    }

    fun read(): CredentialRealmCache {
        val result = realm.query(CredentialRealmCache::class).find()
        if (result.isEmpty())
            throw AuthException("Для продолжения авторизуйтесь в приложении")
        else
            return result.last()
    }

    fun clearAll() {
        realm.writeBlocking {
            val writeTransactionItems = query<CredentialRealmCache>().find()
            delete(writeTransactionItems)
        }
    }
}
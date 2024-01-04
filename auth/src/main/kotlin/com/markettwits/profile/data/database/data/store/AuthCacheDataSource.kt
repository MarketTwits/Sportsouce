package com.markettwits.profile.data.database.data.store

import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.entities.UserSettingsRealmCache
import io.realm.kotlin.ext.query

class AuthCacheDataSource(
    private val realmProvider: RealmDatabaseProvider,
) {
    private val realm = realmProvider.realm(setOf(UserSettingsRealmCache::class))
    fun write(data: UserSettingsRealmCache) {
        realm.writeBlocking {
            copyToRealm(data)
        }
    }

    fun updatePassword(password: String) {
        realm.writeBlocking {
            findLatest(read())?._password = password
        }
    }


    fun read(): UserSettingsRealmCache {
        val result = realm.query(UserSettingsRealmCache::class).find()
        if (result.isEmpty()) {
            throw AuthException("Для продолжения авторизуйтесь в приложении")
        } else {
            return result.last()
        }
    }


    fun clearAll() {
        realm.writeBlocking {
            val writeTransactionItems = query<UserSettingsRealmCache>().find()
            delete(writeTransactionItems)
        }
    }

    fun delete(id: Long) {
        realm.writeBlocking {
            val item = query<UserSettingsRealmCache>(query = "_id == $0", id).first().find()
            try {
                item?.let { delete(it) }
            } catch (e: Exception) {
                throw RuntimeException("ImagesCacheDataSource#delete" + e.localizedMessage)
            }
        }
    }
}
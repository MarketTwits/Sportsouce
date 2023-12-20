package com.markettwits.data.store

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

    fun read(): UserSettingsRealmCache {
        val result = realm.query(UserSettingsRealmCache::class).find()
        return result.first()
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
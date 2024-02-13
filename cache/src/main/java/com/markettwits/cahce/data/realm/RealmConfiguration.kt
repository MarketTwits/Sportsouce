package com.markettwits.cahce.data.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmObject

interface RealmConfiguration {
    fun open(): Realm
    fun entity(): RealmObject
}

abstract class RealmConfigurationBase(
    private val provider: RealmDatabaseProvider,
    private val entity: RealmObject
) : RealmConfiguration {
    override fun open(): Realm =
        provider.realm(setOf(entity::class))

    override fun entity(): RealmObject = entity
}
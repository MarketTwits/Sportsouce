package com.markettwits.cahce.data.realm

import io.realm.kotlin.types.RealmObject

abstract class RealmCachedEntity : RealmObject {
    abstract var id: Long
    abstract var name: String
}
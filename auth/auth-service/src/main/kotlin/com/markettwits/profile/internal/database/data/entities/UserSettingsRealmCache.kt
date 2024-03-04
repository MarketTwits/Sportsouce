package com.markettwits.profile.internal.database.data.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlin.random.Random


internal class CredentialRealmCache : RealmObject {
    @PrimaryKey
    var _id: Long = Random.nextLong()
    var _userId: Long = 0
    var _email: String = ""
    var _password: String = ""
    var _accessToken : String = ""
    var _refreshToken : String = ""
}



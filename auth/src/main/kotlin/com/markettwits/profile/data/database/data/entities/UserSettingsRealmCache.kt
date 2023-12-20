package com.markettwits.profile.data.database.data.entities

import com.plcoding.androidcrypto.UserSettings
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlin.random.Random

class UserSettingsRealmCache : RealmObject {
    @PrimaryKey
    var _id: Long = Random.nextLong()
    var _email: String = ""
    var _password: String = ""
    var _accessToken : String = ""
    var _refreshToken : String = ""
    fun map() = UserSettings(
        id = _id,
        email = _email,
        password = _password,
        accessToken = _accessToken,
        refreshToken = _refreshToken)
}
package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultRemote(
    val cell: String,
    val dob: DobRemote,
    val email: String,
    val gender: String,
    val id: IdRemote,
    val location: LocationRemote,
    val login: LoginRemote,
    val name: NameRemote,
    val nat: String,
    val phone: String,
    val picture: PictureRemote,
    val registered: RegisteredRemote
){
    @Serializable
    data class RegisteredRemote(
        val age: Int,
        val date: String
    )
    @Serializable
    data class PictureRemote(
        val large: String,
        val medium: String,
        val thumbnail: String
    )@Serializable
    data class NameRemote(
        val first: String,
        val last: String,
        val title: String
    )
    @Serializable
    data class LoginRemote(
        val md5: String,
        val password: String,
        val salt: String,
        val sha1: String,
        val sha256: String,
        val username: String,
        val uuid: String
    )
}
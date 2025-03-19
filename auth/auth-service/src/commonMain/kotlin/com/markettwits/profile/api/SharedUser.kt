package com.markettwits.profile.api

import com.markettwits.auth.cloud.model.sign_in.response.User
import kotlinx.serialization.Serializable

@Serializable
data class SharedUser(
    val id : Int,
    val age : String,
    val name : String,
    val surname : String,
    val phone : String,
    val imageUrl : String,
)

fun User.mapToShared() : SharedUser = SharedUser(
    id = id,
    age = age ?: "",
    name = name,
    phone = number,
    surname = surname,
    imageUrl = when(photo){
        is User.Photo.EmptyPhoto -> ""
        is User.Photo.WithPhoto -> (photo as User.Photo.WithPhoto).imageUrl()
        null -> ""
    }
)
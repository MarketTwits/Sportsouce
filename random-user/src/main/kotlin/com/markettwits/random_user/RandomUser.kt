package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class RandomUser(
    val cell: String,
    val ageParam: AgeParam,
    val email: String,
    val gender: String,
    val id: String,
    val location: Location,
    val name: Name,
    val phone: String,
    val picture: String,
)
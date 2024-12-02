package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationOld(
    val distance: String,
    val email: String?,
    val format: String,
    val group: String?,
    val id: Int,
    val name: String,
    val payment: Int?,
    val price: Int?,
    val start: UserRegistrationStart,
    val start_id: Int,
    val surname: String,
    val team: String,
    val user_id: Int,
) : UserRegistrationShared

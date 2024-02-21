package com.markettwits.cloud.model.start_user

import kotlinx.serialization.Serializable

@Serializable
data class RemoteStartsUserItem(
    val day: Int,
    val distance: String,
    val email: String,
    val format: String,
    val group: String?,
    val id: Int,
    val name: String,
    val payment: Int?,
    val price: Int,
    val start: RemoteStartUser,
    val start_id: Int,
    val surname: String,
    val team: String,
    val user_id: Int,
)
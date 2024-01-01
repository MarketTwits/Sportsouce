package com.markettwits.cloud.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoRequest(
    val id : Int,
    val address: String,
    val birthday: String,
    val comment_for_address: String?,
    val email: String,
    val favor: String?,
    val instagram: String?,
    val name: String,
    val number: String,
    val photo_id: String?,
    val sex: String,
    val surname: String,
    val team: String?,
    val telegram: String?,
    val vk: String?,
    val whatsapp: String?
)


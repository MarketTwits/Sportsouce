package com.markettwits.cloud.model.auth.sign_in.response

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val address: String,
    val age: String?,
    val birthday: String,
    val comment_for_address: String?,
    val createdAt: String,
    val email: String,
    val favor: String?,
    val id: Int,
    val instagram: String?,
    val name: String,
    val number: String,
    val patronymic: String?,
    val photo_id: String?,
    val role: String,
    val sex: String,
    val surname: String,
    val team: String?,
    val telegram: String?,
    val updatedAt: String,
    val verification_code: String,
    val verified: Boolean,
    val vk: String?,
    val whatsapp: String?
)
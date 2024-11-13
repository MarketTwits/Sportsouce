package com.markettwits.cloud_shop.model.orders

import kotlinx.serialization.Serializable

@Serializable
data class UserRecipient(
    val email: String,
    val id: Int,
    val name: String,
    val number: String,
    val surname: String
)
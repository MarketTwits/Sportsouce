package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactsRemote(
    val info: InfoRemote,
    val results: List<ResultRemote>
)
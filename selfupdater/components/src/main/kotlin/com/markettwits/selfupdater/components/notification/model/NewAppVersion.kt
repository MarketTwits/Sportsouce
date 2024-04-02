package com.markettwits.selfupdater.components.notification.model

import kotlinx.serialization.Serializable

@Serializable
data class NewAppVersion(
    val version: String,
    val description: String
)
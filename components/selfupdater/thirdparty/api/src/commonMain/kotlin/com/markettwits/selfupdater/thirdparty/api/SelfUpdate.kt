package com.markettwits.selfupdater.thirdparty.api

import kotlinx.serialization.Serializable

@Serializable
data class SelfUpdate(
    val version: String,
    val downloadUrl: String,
    val name: String,
    val description: String,
    val sizeBytes: Long? = null,
    val sha256: String? = null,
)

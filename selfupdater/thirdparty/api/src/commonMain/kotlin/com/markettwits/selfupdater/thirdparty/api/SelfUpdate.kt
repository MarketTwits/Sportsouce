package com.markettwits.selfupdater.thirdparty.api

data class SelfUpdate(
    val version: String,
    val downloadUrl: String,
    val name: String,
    val description: String
)

package com.markettwits.version

data class ApplicationVersion(
    val versionName: String,
    val versionBuildNumber: String? = null,
)

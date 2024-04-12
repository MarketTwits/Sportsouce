package com.flipperdevices.selfupdater.models


sealed interface SelfUpdateResult {
    data class SelfUpdateReady(val versionName: String, val description: String) : SelfUpdateResult
    data object Complete : SelfUpdateResult
    data object NoUpdates : SelfUpdateResult
    data object InProgress : SelfUpdateResult
    data class Error(val exception: Exception) : SelfUpdateResult
}


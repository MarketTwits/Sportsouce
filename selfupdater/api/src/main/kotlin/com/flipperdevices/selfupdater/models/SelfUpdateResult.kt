package com.flipperdevices.selfupdater.models


sealed interface SelfUpdateResult {
    data object COMPLETE : SelfUpdateResult
    data object NO_UPDATES : SelfUpdateResult
    data object IN_PROGRESS : SelfUpdateResult
    data class ERROR(val exception: Exception) : SelfUpdateResult
}


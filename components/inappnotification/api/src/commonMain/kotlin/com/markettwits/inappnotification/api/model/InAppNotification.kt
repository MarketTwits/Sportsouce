package com.markettwits.inappnotification.api.model


sealed interface InAppNotification {

    data object SelfUpdateStarted : InAppNotification

    data class SelfUpdateError(val exception: Exception) : InAppNotification

    data class HiddenApp(val action: () -> Unit) : InAppNotification

    data class SelfUpdateReady(
        val action: () -> Unit,
        val actualVersion: String,
        val description: String
    ) : InAppNotification

    data class Error(
        val titleId: Int,
        val descId: Int,
        val actionTextId: Int?,
        val action: (() -> Unit)?,
    ) : InAppNotification
}

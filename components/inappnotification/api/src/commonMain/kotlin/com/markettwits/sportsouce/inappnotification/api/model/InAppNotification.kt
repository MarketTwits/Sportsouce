package com.markettwits.sportsouce.inappnotification.api.model


enum class UpdateFailureKind { General, UnsupportedDevice }

sealed interface InAppNotification {

    data object SelfUpdateStarted : InAppNotification

    data class SelfUpdateProgress(val percent: Int?, val paused: Boolean = false) : InAppNotification

    data class SelfUpdateError(val exception: Exception, val kind: UpdateFailureKind = UpdateFailureKind.General) : InAppNotification

    /** Internal event: a previously offered APK is no longer available for installation. */
    data class SelfUpdateCleared(val releaseId: String) : InAppNotification

    data class HiddenApp(val action: () -> Unit) : InAppNotification

    data class SelfUpdateReady(
        val action: () -> Unit,
        val actualVersion: String,
        val description: String,
        val readyToInstall: Boolean = false,
        val informationalOnly: Boolean = false,
        val desktopInstaller: Boolean = false,
        val desktopDiskImage: Boolean = false,
        val onDismiss: () -> Unit = {},
        val releaseId: String? = null,
    ) : InAppNotification

    data class Error(
        val titleId: Int,
        val descId: Int,
        val actionTextId: Int?,
        val action: (() -> Unit)?,
    ) : InAppNotification
}

package com.markettwits.selfupdater.models


sealed interface SelfUpdateResult {
    data class SelfUpdateReady(
        val versionName: String,
        val description: String,
        val informationalOnly: Boolean = false,
        val releaseId: String? = null,
    ) : SelfUpdateResult
    data object Complete : SelfUpdateResult
    data object NoUpdates : SelfUpdateResult
    data object InProgress : SelfUpdateResult
    data object DownloadInProgress : SelfUpdateResult
    /** A native updater is interacting with the user; installation is not yet confirmed. */
    data object InteractiveUpdateInProgress : SelfUpdateResult
    data class DownloadProgress(val percent: Int?, val paused: Boolean) : SelfUpdateResult {
        init { require(percent == null || percent in 0..99) }
    }
    data object ReadyToInstall : SelfUpdateResult
    data object DesktopInstallerDownloaded : SelfUpdateResult
    data object Unsupported : SelfUpdateResult
    data class Error(val exception: Exception) : SelfUpdateResult
}


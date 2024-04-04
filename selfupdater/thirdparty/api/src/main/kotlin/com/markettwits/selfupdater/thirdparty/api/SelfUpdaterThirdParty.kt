package com.markettwits.selfupdater.thirdparty.api

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.flipperdevices.selfupdater.models.SemVer
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.markettwits.inappnotification.api.model.InAppNotification


class SelfUpdaterThirdParty(
    private val context: Context,
    private val updateParser: SelfUpdateParserApi,
    private val inAppNotificationStorage: InAppNotificationStorage,
    private val version: String
) : SelfUpdaterSourceApi {

    private val nameParser = updateParser.getName()

    override fun getInstallSourceName() = nameParser

    override fun isSelfUpdateCanManualCheck(): Boolean = true

    private var downloadId: Long? = null
    private val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {

        val lastReleaseResult = runCatching { processCheckUpdate(version) }
        val lastReleaseException = lastReleaseResult.exceptionOrNull()
        if (lastReleaseException != null) {
            if (manual) {
                inAppNotificationStorage.addNotification(
                    InAppNotification.SelfUpdateError(
                        exception = IllegalArgumentException("New update version is not found")
                    )
                )
            }
            return SelfUpdateResult.ERROR(IllegalStateException("Error while check update $lastReleaseException"))
        }
        val lastRelease = lastReleaseResult.getOrNull() ?: return SelfUpdateResult.NO_UPDATES

        val readyUpdateNotification = InAppNotification.SelfUpdateReady(
            actualVersion = lastRelease.version,
            action = {
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateStarted)
                downloadFile(lastRelease, context, manual)
            },
            description = lastRelease.description
        )
        if (manual) {
            readyUpdateNotification.action()
        }
        inAppNotificationStorage.addNotification(readyUpdateNotification)

        return SelfUpdateResult.COMPLETE
    }

    private suspend fun processCheckUpdate(version: String): SelfUpdate? {
        val lastRelease =
            updateParser.getLastUpdate() ?: error { "No release found for github update" }
        val currentVersion = checkNotNull(SemVer.fromString(version))
        val newVersion = checkNotNull(SemVer.fromString(lastRelease.version))
        if (currentVersion >= newVersion) {
            return null
        }
        return lastRelease
    }

    private fun registerDownloadReceiver(context: Context) {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                downloadReceiver,
                intentFilter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            context.registerReceiver(downloadReceiver, intentFilter)
        }
    }

    private fun downloadFile(githubUpdate: SelfUpdate, context: Context, manual: Boolean) {
        try {
            val url = githubUpdate.downloadUrl
            val title = githubUpdate.name
            Log.e("mt05", "DOWNLOAD TITLE : $title")
            val networkTypes =
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            val request = DownloadManager.Request(Uri.parse(url))
                .setAllowedNetworkTypes(networkTypes)
                .setTitle(title)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title)
            this.downloadId = manager.enqueue(request)
        } catch (e: Exception) {
            if (manual) {
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(exception = e))
            }
            error { "Error while download update $e" }
        }
        registerDownloadReceiver(context)
    }

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intents: Intent) {
            try {
                val currentDownloadId = intents.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId != currentDownloadId) {
                    inAppNotificationStorage.addNotification(
                        InAppNotification.SelfUpdateError(
                            IllegalArgumentException("downloadId != currentDownloadId")
                        )
                    )
                    throw RuntimeException("downloadId == currentDownloadId")
                }

                val uri = manager.getUriForDownloadedFile(requireNotNull(downloadId))

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    setDataAndType(uri, "application/vnd.android.package-archive")
                }
                context.startActivity(intent)
                context.unregisterReceiver(this)
            } catch (e: Exception) {
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(e))
            }
        }
    }
}

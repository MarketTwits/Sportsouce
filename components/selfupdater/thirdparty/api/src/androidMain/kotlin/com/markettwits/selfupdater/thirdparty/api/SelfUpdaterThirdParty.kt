package com.markettwits.selfupdater.thirdparty.api

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.api.model.UpdateFailureKind
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

/** Android sideload adapter. The system installer is launched only by an explicit user action. */
class SelfUpdaterThirdParty(
    context: Context,
    private val updateParser: SelfUpdateParserApi,
    private val inAppNotificationStorage: InAppNotificationStorage,
    private val queryDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SelfUpdaterSourceApi {
    private val context = context.applicationContext
    private val manager = this.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val preferences = this.context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
    private val verifier = AndroidApkVerifier(this.context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var receiverRegistered = false
    private var progressJob: Job? = null
    private var reportedProgress: InAppNotification.SelfUpdateProgress? = null
    private var lastReadyVersion: String? = null

    override fun getInstallSourceName(): String = updateParser.getName()
    override fun isSelfUpdateCanManualCheck(): Boolean = true

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        val existing = reconcile()
        if (existing != null) return existing
        val release = try {
            updateParser.getLastUpdate() ?: return SelfUpdateResult.NoUpdates
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            return SelfUpdateResult.Error(IllegalStateException("Error while checking for updates", failure))
        }
        val version = ReleaseVersion.fromTag(release.version)
            ?: return SelfUpdateResult.Error(IllegalArgumentException("Invalid release version"))
        if (context.getPackageInfo().buildNumber() >= version.buildNumber) return SelfUpdateResult.NoUpdates
        if (release.androidDownloadMetadata() == null) {
            return SelfUpdateResult.Error(IllegalArgumentException("Invalid APK metadata"))
        }
        if (!manual) return SelfUpdateResult.SelfUpdateReady(release.version, release.description, releaseId = release.version)
        return startDownload(release)
    }

    override suspend fun resumeUpdate(): SelfUpdateResult = reconcile() ?: SelfUpdateResult.NoUpdates

    override fun pendingRelease(): SelfUpdateResult.SelfUpdateReady? =
        runCatching { readPending() }.getOrNull()?.let {
            SelfUpdateResult.SelfUpdateReady(it.version, it.description, releaseId = it.version)
        }

    override suspend fun installUpdate(): SelfUpdateResult {
        val pending = runCatching { readPending() }.getOrNull()?.takeIf { it.ready }
            ?: return SelfUpdateResult.NoUpdates
        return install(pending)
    }

    private suspend fun startDownload(release: SelfUpdate): SelfUpdateResult = withContext(Dispatchers.Main.immediate) {
        mutex.withLock {
            if (readPending() != null) return@withLock SelfUpdateResult.DownloadInProgress
            val metadata = release.androidDownloadMetadata()
                ?: return@withLock SelfUpdateResult.Error(IllegalArgumentException("Invalid APK metadata"))
            val expectedName = metadata.fileName

            try {
                val source = downloadFile(expectedName)
                if (source.exists() && !source.delete()) error("Unable to remove an old download")
                registerReceiver()
                val request = DownloadManager.Request(Uri.parse(release.downloadUrl))
                    .setTitle(expectedName)
                    // Completion is offered only after verification; DownloadManager must not expose the raw APK.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, expectedName)
                val id = manager.enqueue(request)
                val pending = PendingDownload(id, release.version, expectedName, release.description, metadata.sizeBytes, metadata.sha256, false)
                if (!writePending(pending)) {
                    manager.remove(id)
                    error("Unable to save the download")
                }
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateStarted)
                // A very fast completion may arrive before the ID is persisted.
                scope.launch { reconcileInBackground() }
                SelfUpdateResult.DownloadProgress(null, false)
            } catch (failure: Exception) {
                unregisterReceiver()
                SelfUpdateResult.Error(IllegalStateException("Unable to start the update download", failure))
            }
        }
    }

    private suspend fun reconcile(): SelfUpdateResult? = withContext(Dispatchers.Main.immediate) {
        mutex.withLock {
            val pending = runCatching { readPending() }.getOrNull() ?: run {
                if (!preferences.contains("id")) return@withLock null
                // The saved record is incomplete or invalid. Do not use its path,
                // and do not report "no updates" for a broken operation.
                val id = runCatching { preferences.getLong("id", -1) }.getOrDefault(-1)
                if (id >= 0) runCatching { manager.remove(id) }
                unregisterReceiver()
                preferences.edit().clear().commit()
                val error = IllegalStateException("Saved update download is invalid")
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(error))
                return@withLock SelfUpdateResult.Error(error)
            }
            val version = ReleaseVersion.fromTag(pending.version) ?: return@withLock failPending(pending, "Invalid saved version")
            if (context.getPackageInfo().buildNumber() >= version.buildNumber) {
                clearPending(pending)
                return@withLock SelfUpdateResult.NoUpdates
            }
            if (pending.ready) {
                // The process may have died or the staged file may have disappeared or changed.
                // Do not tell the user it is ready until the current file is trusted again.
                val file = File(File(context.filesDir, "self-updater"), pending.name)
                val expected = ApkDownloadExpectation(version.buildNumber, pending.sizeBytes, pending.sha256)
                when (val result = verifier.verify(file, expected)) {
                    ApkFileVerification.Verified -> Unit
                    is ApkFileVerification.Rejected -> return@withLock rejectPending(pending, result.reason)
                    is ApkFileVerification.Unreadable -> return@withLock failPending(pending, "Saved APK verification failed")
                }
                // Handle a crash after persisting ready but before removing the DownloadManager job.
                runCatching { manager.remove(pending.id) }
                unregisterReceiver()
                return@withLock presentReady(pending)
            }
            registerReceiver()
            val download = try {
                withContext(queryDispatcher) {
                    manager.query(DownloadManager.Query().setFilterById(pending.id)).use { cursor ->
                        if (cursor == null || !cursor.moveToFirst()) null
                        else Triple(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)),
                        )
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                return@withLock reportPendingError("Unable to inspect the download", failure)
            }
            when (download?.first) {
                DownloadManager.STATUS_PENDING, DownloadManager.STATUS_RUNNING, DownloadManager.STATUS_PAUSED -> {
                    val (_, bytes, total) = download
                    if (bytes > pending.sizeBytes || total > pending.sizeBytes) {
                        return@withLock failPending(pending, "Download exceeds the expected APK size")
                    }
                    val result = if (total > 0 && bytes >= 0 && total == pending.sizeBytes) {
                        val percent = ((bytes.coerceAtMost(total).toDouble() / total) * 100).toInt().coerceIn(0, 99)
                        SelfUpdateResult.DownloadProgress(percent, download.first == DownloadManager.STATUS_PAUSED)
                    } else SelfUpdateResult.DownloadProgress(null, download.first == DownloadManager.STATUS_PAUSED)
                    val progress = InAppNotification.SelfUpdateProgress(result.percent, result.paused)
                    if (progress != reportedProgress) {
                        inAppNotificationStorage.addNotification(progress)
                        reportedProgress = progress
                    }
                    if (progressJob?.isActive != true) progressJob = scope.launch {
                        while (runCatching { readPending()?.ready == false }.getOrDefault(false)) {
                            delay(2_000)
                            reconcileInBackground()
                        }
                    }
                    result
                }
                DownloadManager.STATUS_SUCCESSFUL -> stageAndVerify(pending, version)
                DownloadManager.STATUS_FAILED, null -> failPending(pending, "The update download failed")
                else -> reportPendingError("Unknown download status")
            }
        }
    }

    private suspend fun stageAndVerify(pending: PendingDownload, version: ReleaseVersion): SelfUpdateResult {
        val directory = File(context.filesDir, "self-updater")
        val staged = File(directory, "${pending.name}.part.apk")
        val target = File(directory, pending.name)
        try {
            withContext(Dispatchers.IO) {
                check(directory.isDirectory || directory.mkdirs())
                val source = downloadFile(pending.name)
                check(source.isFile && source.length() == pending.sizeBytes)
                source.inputStream().buffered().use { input ->
                    staged.outputStream().buffered().use { output ->
                        val buffer = ByteArray(8192)
                        var copied = 0L
                        while (true) {
                            currentCoroutineContext().ensureActive()
                            val count = input.read(buffer)
                            if (count < 0) break
                            copied += count
                            check(copied <= pending.sizeBytes)
                            output.write(buffer, 0, count)
                        }
                        check(copied == pending.sizeBytes)
                    }
                }
            }
            val expected = ApkDownloadExpectation(version.buildNumber, pending.sizeBytes, pending.sha256)
            when (val result = verifier.verify(staged, expected)) {
                ApkFileVerification.Verified -> Unit
                is ApkFileVerification.Rejected -> return rejectPending(pending, result.reason)
                is ApkFileVerification.Unreadable -> return failPending(pending, "Unable to read the downloaded APK")
            }
            withContext(Dispatchers.IO) {
                if (target.exists()) check(target.delete())
                check(staged.renameTo(target))
            }
            val ready = pending.copy(ready = true)
            if (!writePending(ready)) error("Unable to save the verified update")
            // The verified APK and ready state are already durable; DownloadManager cleanup
            // failure must not discard them. Reconciliation will retry cleanup later.
            runCatching { manager.remove(pending.id) }
            unregisterReceiver()
            return presentReady(ready)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            // A completed DownloadManager job with an unreadable or missing source cannot
            // make progress on its own. Release it so the user can start a fresh download.
            return failPending(pending, "Unable to prepare the update", failure)
        } finally {
            withContext(NonCancellable + Dispatchers.IO) { if (staged.exists()) staged.delete() }
        }
    }

    private fun presentReady(pending: PendingDownload): SelfUpdateResult {
        if (lastReadyVersion != pending.version) {
            inAppNotificationStorage.addNotification(
                InAppNotification.SelfUpdateReady(
                    actualVersion = pending.version,
                    description = pending.description,
                    readyToInstall = true,
                    releaseId = pending.version,
                    action = { scope.launch { install(pending) } },
                    onDismiss = { if (lastReadyVersion == pending.version) lastReadyVersion = null },
                )
            )
            lastReadyVersion = pending.version
        }
        return SelfUpdateResult.ReadyToInstall
    }

    private suspend fun install(pending: PendingDownload) = withContext(Dispatchers.Main.immediate) {
        mutex.withLock {
            try {
                lastReadyVersion = null
                if (readPending() != pending) return@withLock SelfUpdateResult.NoUpdates
                val version = ReleaseVersion.fromTag(pending.version) ?: return@withLock SelfUpdateResult.NoUpdates
                if (context.getPackageInfo().buildNumber() >= version.buildNumber) {
                    clearPending(pending)
                    return@withLock SelfUpdateResult.NoUpdates
                }
                val file = File(File(context.filesDir, "self-updater"), pending.name)
                val expected = ApkDownloadExpectation(version.buildNumber, pending.sizeBytes, pending.sha256)
                when (val result = verifier.verify(file, expected)) {
                    ApkFileVerification.Verified -> Unit
                    is ApkFileVerification.Rejected -> return@withLock rejectPending(pending, result.reason)
                    is ApkFileVerification.Unreadable -> return@withLock failPending(pending, "APK verification failed before installation")
                }
                // Verification may take long enough for the app to leave the foreground.
                val activity = CurrentActivityHolder.getCurrentActivity()
                check(activity != null && !activity.isFinishing && !activity.isDestroyed) { "A resumed activity is required" }
                if (!context.packageManager.canRequestPackageInstalls()) {
                    activity.startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}")))
                    return@withLock SelfUpdateResult.ReadyToInstall
                }
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.selfupdater.files", file)
                val intent = Intent(Intent.ACTION_VIEW).setDataAndType(uri, "application/vnd.android.package-archive")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .also { it.clipData = ClipData.newRawUri("update", uri) }
                activity.startActivity(intent)
                SelfUpdateResult.ReadyToInstall
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(failure))
                if (runCatching { readPending() }.getOrNull() == pending) presentReady(pending)
                SelfUpdateResult.Error(failure)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE ||
                intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) != runCatching { readPending()?.id }.getOrNull()
            ) return
            val completion = goAsync()
            // The OS broadcast has a short deadline, but hashing and inspecting a
            // large APK may take longer. Keep work alive in the application scope.
            val work = scope.launch { reconcileInBackground() }
            scope.launch {
                finishDownloadBroadcast(work) { completion.finish() }
            }
        }
    }

    private suspend fun reconcileInBackground() {
        try {
            reconcile()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(failure))
        }
    }

    private fun registerReceiver() {
        if (receiverRegistered) return
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        else @Suppress("UnspecifiedRegisterReceiverFlag") context.registerReceiver(receiver, filter)
        receiverRegistered = true
    }

    private fun unregisterReceiver() {
        if (!receiverRegistered) return
        context.unregisterReceiver(receiver)
        receiverRegistered = false
    }

    private fun rejectPending(pending: PendingDownload, reason: ApkVerificationFailure): SelfUpdateResult {
        if (reason == ApkVerificationFailure.UnsupportedAndroidVersion || reason == ApkVerificationFailure.UnsupportedArchitecture) {
            clearPending(pending)
            inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(
                IllegalStateException("APK verification failed: $reason"), UpdateFailureKind.UnsupportedDevice,
            ))
            return SelfUpdateResult.Unsupported
        }
        return failPending(pending, "APK verification failed: $reason")
    }

    private fun failPending(pending: PendingDownload, message: String, cause: Exception? = null): SelfUpdateResult {
        clearPending(pending)
        return reportPendingError(message, cause)
    }

    private fun reportPendingError(message: String, cause: Exception? = null): SelfUpdateResult {
        val error = IllegalStateException(message, cause)
        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(error))
        return SelfUpdateResult.Error(error)
    }

    private fun clearPending(pending: PendingDownload) {
        runCatching { manager.remove(pending.id) }
        unregisterReceiver()
        preferences.edit().clear().commit()
        lastReadyVersion = null
        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateCleared(pending.version))
        // DownloadManager normally deletes its file on remove, but the job may
        // already be gone. The name was validated when restoring the record.
        runCatching { downloadFile(pending.name).delete() }
        File(File(context.filesDir, "self-updater"), pending.name).delete()
        File(File(context.filesDir, "self-updater"), "${pending.name}.part.apk").delete()
    }

    private fun downloadFile(name: String): File = File(
        requireNotNull(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)), name,
    )

    private fun readPending(): PendingDownload? {
        val id = preferences.getLong("id", -1)
        if (id < 0) return null
        val version = preferences.getString("version", null) ?: return null
        val parsed = ReleaseVersion.fromTag(version) ?: return null
        val name = preferences.getString("name", null) ?: return null
        if (name != "Sportsauce-${parsed.versionName}-${parsed.buildNumber}.apk") return null
        val size = preferences.getLong("size", -1)
        val sha256 = preferences.getString("sha256", null)
        if (size <= 0 || !sha256.isSha256()) return null
        return PendingDownload(id, version, name, preferences.getString("description", "").orEmpty(), size, requireNotNull(sha256), preferences.getBoolean("ready", false))
    }

    private fun writePending(pending: PendingDownload): Boolean = preferences.edit()
        .putLong("id", pending.id)
        .putString("version", pending.version)
        .putString("name", pending.name)
        .putString("description", pending.description)
        .putLong("size", pending.sizeBytes)
        .putString("sha256", pending.sha256)
        .putBoolean("ready", pending.ready)
        .commit()

    private data class PendingDownload(
        val id: Long,
        val version: String,
        val name: String,
        val description: String,
        val sizeBytes: Long,
        val sha256: String,
        val ready: Boolean,
    )

    private companion object {
        // Settings and the notification host have separate Koin scopes but share DownloadManager state.
        val mutex = Mutex()
    }
}

internal suspend fun finishDownloadBroadcast(work: Job, timeoutMillis: Long = 8_000, finish: () -> Unit) {
    try {
        withTimeoutOrNull(timeoutMillis) { work.join() }
    } finally {
        finish()
    }
}

private fun String?.isSha256(): Boolean = this != null && length == 64 && all { it in '0'..'9' || it in 'a'..'f' }

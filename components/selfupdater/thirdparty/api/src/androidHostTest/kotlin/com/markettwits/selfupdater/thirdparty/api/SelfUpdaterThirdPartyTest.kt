package com.markettwits.selfupdater.thirdparty.api

import android.app.DownloadManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.Looper
import android.content.pm.PackageInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.pm.SigningInfo
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.api.model.UpdateFailureKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowDownloadManager
import org.robolectric.shadows.ShadowSigningInfo
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SelfUpdaterThirdPartyTest {
    @Test
    fun oversizedDownloadIsCancelledAndCanBeRetried() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloads = Shadow.extract<ShadowDownloadManager>(manager)
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val progress = Shadow.extract<ShadowDownloadManager.ShadowRequest>(downloads.getRequest(id))
            progress.status = DownloadManager.STATUS_RUNNING
            progress.totalSize = 5
            assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
            assertEquals(-1, prefs.getLong("id", -1))
            assertEquals(0, downloads.requestCount)
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun missingDownloadManagerJobAfterProcessRecreationAllowsRetry() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val first = source(context, Storage())
            assertIs<SelfUpdateResult.DownloadProgress>(first.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val originalId = prefs.getLong("id", -1)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.remove(originalId)
            val leftover = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name)
            leftover.writeBytes(byteArrayOf(1, 2, 3, 4))

            val storage = Storage()
            val recreated = source(context, storage)
            assertIs<SelfUpdateResult.Error>(recreated.resumeUpdate())
            assertEquals(-1, prefs.getLong("id", -1))
            assertTrue(!leftover.exists())
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertTrue(storage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertIs<SelfUpdateResult.DownloadProgress>(recreated.checkUpdate(manual = true))
            assertTrue(prefs.getLong("id", -1) >= 0)
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun unknownDownloadStatusReportsErrorButKeepsOperationForRetry() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = Shadow.extract<ShadowDownloadManager>(manager).getRequest(id)
            val shadow = Shadow.extract<ShadowDownloadManager.ShadowRequest>(request)
            shadow.status = 99
            assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertEquals(id, prefs.getLong("id", -1))
            shadow.status = DownloadManager.STATUS_RUNNING
            assertIs<SelfUpdateResult.DownloadProgress>(updater.resumeUpdate())
            assertEquals(id, prefs.getLong("id", -1))
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun invalidSavedDownloadReportsFailureAndAllowsRetry() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val storage = Storage()
            val updater = source(context, storage)
            prefs.edit().putString("id", "not a number").putString("name", "../outside.apk").commit()
            assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
            assertTrue(!prefs.contains("id"))
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            assertTrue(prefs.getLong("id", -1) >= 0)
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun slowApkVerificationContinuesAfterBroadcastDeadline() = runTest {
        var verified = false
        var finished = 0
        val work = launch { delay(10_000); verified = true }
        val broadcast = launch { finishDownloadBroadcast(work) { finished++ } }
        runCurrent()
        advanceTimeBy(8_001)
        runCurrent()
        assertEquals(1, finished)
        assertTrue(work.isActive)
        assertTrue(!verified)
        advanceTimeBy(2_000)
        runCurrent()
        assertTrue(verified)
        assertTrue(broadcast.isCompleted)
        assertEquals(1, finished)
    }

    private val release = SelfUpdate(
        version = "1.6.1.10422",
        name = "Sportsauce-1.6.1-10422.apk",
        downloadUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/Sportsauce-1.6.1-10422.apk",
        description = "Changes",
        sizeBytes = 4,
        sha256 = "a".repeat(64),
    )

    private class Storage : InAppNotificationStorage {
        val notifications = mutableListOf<InAppNotification>()
        override fun subscribe(listener: InAppNotificationListener) = Unit
        override fun unsubscribe() = Unit
        override fun addNotification(notification: InAppNotification) { notifications += notification }
    }

    private fun source(context: Context, storage: Storage, item: SelfUpdate = release, queryDispatcher: CoroutineDispatcher = Dispatchers.Main): SelfUpdaterThirdParty =
        SelfUpdaterThirdParty(context, object : SelfUpdateParserApi {
            override fun getName() = "GitHub"
            override suspend fun getLastUpdate() = item
        }, storage, queryDispatcher = queryDispatcher)

    @Test
    fun cancelledStatusQueryKeepsDownloadAvailableForRestoration() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloads = Shadow.extract<ShadowDownloadManager>(manager)
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(downloads.getRequest(id)).status = DownloadManager.STATUS_RUNNING
            runCurrent()

            val suspendedUpdater = source(context, storage, queryDispatcher = StandardTestDispatcher(TestCoroutineScheduler()))
            val query = async { suspendedUpdater.resumeUpdate() }
            runCurrent()
            query.cancel()
            query.join()

            assertEquals(id, prefs.getLong("id", -1))
            assertEquals(1, downloads.requestCount)
            assertIs<SelfUpdateResult.DownloadProgress>(source(context, storage).resumeUpdate())
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun rejectsUntrustedMetadataWithoutStartingDownload() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val shadow = Shadow.extract<ShadowDownloadManager>(manager)
            val source = source(context, Storage(), release.copy(downloadUrl = "https://example.com/app.apk"))
            assertIs<SelfUpdateResult.Error>(source.checkUpdate(manual = true))
            assertIs<SelfUpdateResult.Error>(source.checkUpdate(manual = false))
            assertIs<SelfUpdateResult.Error>(source(context, Storage(), release.copy(sha256 = null)).checkUpdate(manual = false))
            assertEquals(0, shadow.requestCount)
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun restoresPendingDownloadIgnoresOtherBroadcastsAndAllowsRetryAfterFailure() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val shadow = Shadow.extract<ShadowDownloadManager>(manager)
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            assertTrue(id >= 0)
            val request = shadow.getRequest(id)
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_RUNNING
            runCurrent()

            context.sendBroadcast(Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE).putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id + 100))
            shadowOf(Looper.getMainLooper()).idle()
            runCurrent()
            assertEquals(id, prefs.getLong("id", -1))
            assertEquals(1, shadow.requestCount)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            assertIs<SelfUpdateResult.DownloadProgress>(source(context, storage).resumeUpdate())
            assertEquals(1, shadow.requestCount)

            val progress = Shadow.extract<ShadowDownloadManager.ShadowRequest>(request)
            progress.totalSize = release.sizeBytes!!
            progress.bytesSoFar = 2
            assertEquals(SelfUpdateResult.DownloadProgress(50, false), updater.resumeUpdate())
            progress.status = DownloadManager.STATUS_PAUSED
            assertEquals(SelfUpdateResult.DownloadProgress(50, true), updater.resumeUpdate())
            progress.bytesSoFar = 4
            assertEquals(SelfUpdateResult.DownloadProgress(99, true), updater.resumeUpdate())
            progress.totalSize = -1
            assertEquals(SelfUpdateResult.DownloadProgress(null, true), updater.resumeUpdate())
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_FAILED
            context.sendBroadcast(Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE).putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id))
            shadowOf(Looper.getMainLooper()).idle()
            runCurrent()
            assertEquals(-1, prefs.getLong("id", -1))
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun corruptCompletedFileIsNeverOfferedForInstallation() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val shadow = Shadow.extract<ShadowDownloadManager>(manager)
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val request = shadow.getRequest(id)
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name).writeBytes(byteArrayOf(1, 2, 3, 4))
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_SUCCESSFUL
            assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertTrue(storage.notifications.none { it is InAppNotification.SelfUpdateReady && it.readyToInstall })
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun missingCompletedDownloadReportsErrorAndAllowsFreshAttempt() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloads = Shadow.extract<ShadowDownloadManager>(manager)
            val storage = Storage()
            val updater = source(context, storage)
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val request = downloads.getRequest(id)
            runCurrent()
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name).delete()
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_SUCCESSFUL
            context.sendBroadcast(Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE).putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id))
            shadowOf(Looper.getMainLooper()).idle()
            // Await the background staging operation by reconciling under the shared mutex.
            updater.resumeUpdate()
            runCurrent()
            assertEquals(-1, prefs.getLong("id", -1))
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertTrue(storage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            assertTrue(prefs.getLong("id", -1) >= 0)
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun incompatibleNativeApkIsRejectedBeforeInstallerOffer() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val packageShadow = shadowOf(context.packageManager)
            val signature = Signature(byteArrayOf(1, 2, 3, 4))
            fun signing(): SigningInfo = SigningInfo().also {
                Shadow.extract<ShadowSigningInfo>(it).setSignatures(arrayOf(signature))
            }
            packageShadow.getInternalMutablePackageInfo(context.packageName).apply {
                setLongVersionCode(10421)
                signingInfo = signing()
            }
            val archive = PackageInfo().apply {
                packageName = context.packageName
                setLongVersionCode(10422)
                signingInfo = signing()
                applicationInfo = ApplicationInfo().apply { minSdkVersion = 26 }
            }
            val staged = File(File(context.filesDir, "self-updater"), "${release.name}.part.apk")
            packageShadow.setPackageArchiveInfo(staged.absolutePath, archive)
            val bytes = java.io.ByteArrayOutputStream().also { buffer ->
                java.util.zip.ZipOutputStream(buffer).use { zip ->
                    zip.putNextEntry(java.util.zip.ZipEntry("lib/not-a-device-abi/libexample.so"))
                    zip.write(1)
                    zip.closeEntry()
                }
            }.toByteArray()
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
                .joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
            val storage = Storage()
            val updater = source(context, storage, release.copy(sizeBytes = bytes.size.toLong(), sha256 = digest))
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = Shadow.extract<ShadowDownloadManager>(manager).getRequest(id)
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name).writeBytes(bytes)
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_SUCCESSFUL
            assertEquals(SelfUpdateResult.Unsupported, updater.resumeUpdate())
            assertEquals(UpdateFailureKind.UnsupportedDevice, storage.notifications.filterIsInstance<InAppNotification.SelfUpdateError>().last().kind)
            assertTrue(storage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertEquals(-1, prefs.getLong("id", -1))
            assertTrue(!staged.exists())
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun apkRequiringNewerAndroidIsRejectedBeforeInstallerOffer() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val packageShadow = shadowOf(context.packageManager)
            val signature = Signature(byteArrayOf(1, 2, 3, 4))
            fun signing(): SigningInfo = SigningInfo().also {
                Shadow.extract<ShadowSigningInfo>(it).setSignatures(arrayOf(signature))
            }
            packageShadow.getInternalMutablePackageInfo(context.packageName).apply {
                setLongVersionCode(10421)
                signingInfo = signing()
            }
            val archive = PackageInfo().apply {
                packageName = context.packageName
                setLongVersionCode(10422)
                signingInfo = signing()
                applicationInfo = ApplicationInfo().apply { minSdkVersion = 35 }
            }
            val staged = File(File(context.filesDir, "self-updater"), "${release.name}.part.apk")
            packageShadow.setPackageArchiveInfo(staged.absolutePath, archive)
            val bytes = java.io.ByteArrayOutputStream().also { buffer ->
                java.util.zip.ZipOutputStream(buffer).use { zip ->
                    zip.putNextEntry(java.util.zip.ZipEntry("classes.dex"))
                    zip.write(1)
                    zip.closeEntry()
                }
            }.toByteArray()
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
                .joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
            val storage = Storage()
            val updater = source(context, storage, release.copy(sizeBytes = bytes.size.toLong(), sha256 = digest))
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val id = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE).getLong("id", -1)
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = Shadow.extract<ShadowDownloadManager>(manager).getRequest(id)
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name).writeBytes(bytes)
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_SUCCESSFUL
            assertEquals(SelfUpdateResult.Unsupported, updater.resumeUpdate())
            assertEquals(UpdateFailureKind.UnsupportedDevice, storage.notifications.filterIsInstance<InAppNotification.SelfUpdateError>().last().kind)
            assertTrue(storage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertTrue(!staged.exists())
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun verifiedDownloadWaitsForUserAndRestoresAfterPermissionSettings() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val activityController = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = activityController.get()
        CurrentActivityHolder.onActivityResumed(activity)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloads = Shadow.extract<ShadowDownloadManager>(manager)
            val packageShadow = shadowOf(context.packageManager)
            val signature = Signature(byteArrayOf(1, 2, 3, 4))
            fun signing(): SigningInfo = SigningInfo().also {
                Shadow.extract<ShadowSigningInfo>(it).setSignatures(arrayOf(signature))
            }
            packageShadow.getInternalMutablePackageInfo(context.packageName).apply {
                setLongVersionCode(10421)
                signingInfo = signing()
            }
            val archive = PackageInfo().apply {
                packageName = context.packageName
                setLongVersionCode(10422)
                signingInfo = signing()
                applicationInfo = ApplicationInfo().apply { minSdkVersion = 26 }
            }
            val directory = File(context.filesDir, "self-updater")
            val staged = File(directory, "${release.name}.part.apk")
            val verified = File(directory, release.name)
            packageShadow.setPackageArchiveInfo(staged.absolutePath, archive)
            packageShadow.setPackageArchiveInfo(verified.absolutePath, archive)
            val bytes = java.io.ByteArrayOutputStream().also { buffer ->
                java.util.zip.ZipOutputStream(buffer).use { zip ->
                    zip.putNextEntry(java.util.zip.ZipEntry("classes.dex"))
                    zip.write(byteArrayOf(1, 2, 3, 4))
                    zip.closeEntry()
                }
            }.toByteArray()
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
                .joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
            directory.mkdirs()
            staged.writeBytes(bytes)
            assertTrue(context.packageManager.getPackageArchiveInfo(staged.absolutePath, PackageManager.GET_SIGNING_CERTIFICATES) != null, "int flags archive lookup")
            val probe = AndroidApkVerifier(context).verify(staged, ApkDownloadExpectation(10422, bytes.size.toLong(), digest))
            if (probe is ApkFileVerification.Unreadable) throw AssertionError("Verifier could not read the test APK", probe.cause)
            assertIs<ApkFileVerification.Verified>(probe)
            val storage = Storage()
            val updater = source(context, storage, release.copy(sizeBytes = bytes.size.toLong(), sha256 = digest))
            assertIs<SelfUpdateResult.DownloadProgress>(updater.checkUpdate(manual = true))
            runCurrent()
            val prefs = context.getSharedPreferences("self-updater-download", Context.MODE_PRIVATE)
            val id = prefs.getLong("id", -1)
            val request = downloads.getRequest(id)
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), release.name).writeBytes(bytes)
            Shadow.extract<ShadowDownloadManager.ShadowRequest>(request).status = DownloadManager.STATUS_SUCCESSFUL
            val preparation = updater.resumeUpdate()
            if (preparation is SelfUpdateResult.Error) throw AssertionError(preparation.exception.message, preparation.exception)
            assertIs<SelfUpdateResult.ReadyToInstall>(preparation)
            assertTrue(verified.isFile)
            assertEquals(null, shadowOf(activity).peekNextStartedActivity())
            val ready = storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last()
            assertTrue(ready.readyToInstall)
            assertEquals(release.version, ready.releaseId)

            // A corrupted saved record during the click must report an error, not
            // crash the background coroutine or open an installer for untrusted state.
            prefs.edit().putString("id", "broken").commit()
            ready.action()
            runCurrent()
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertEquals(null, shadowOf(activity).peekNextStartedActivity())
            prefs.edit().putLong("id", id).commit()
            storage.notifications.clear()

            ready.onDismiss()
            assertIs<SelfUpdateResult.ReadyToInstall>(updater.resumeUpdate())
            assertEquals(1, storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().size)

            CurrentActivityHolder.onActivityPaused(activity)
            storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertIs<SelfUpdateResult.ReadyToInstall>(updater.resumeUpdate())
            assertEquals(null, shadowOf(activity).peekNextStartedActivity())
            assertTrue(storage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertEquals(2, storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
            CurrentActivityHolder.onActivityResumed(activity)
            packageShadow.setCanRequestPackageInstalls(false)
            storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertIs<SelfUpdateResult.ReadyToInstall>(updater.resumeUpdate())
            assertEquals(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, shadowOf(activity).nextStartedActivity.action)
            assertEquals(3, storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().size)

            // Returning without granting permission retains the verified APK.
            storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertIs<SelfUpdateResult.ReadyToInstall>(updater.resumeUpdate())
            assertEquals(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, shadowOf(activity).nextStartedActivity.action)
            assertEquals(0, downloads.requestCount)
            assertTrue(verified.isFile)

            packageShadow.setCanRequestPackageInstalls(true)
            storage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertIs<SelfUpdateResult.ReadyToInstall>(updater.resumeUpdate())
            val installIntent = shadowOf(activity).nextStartedActivity
            assertEquals(Intent.ACTION_VIEW, installIntent.action)
            assertEquals("application/vnd.android.package-archive", installIntent.type)
            assertEquals("content", installIntent.data?.scheme)
            assertTrue(installIntent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0)
            assertTrue(installIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)

            // Returning without installing (including user cancellation) retains the verified APK.
            val restoredStorage = Storage()
            val restored = source(context, restoredStorage, release.copy(sha256 = digest))
            assertIs<SelfUpdateResult.ReadyToInstall>(restored.resumeUpdate())
            assertEquals(release.version, restored.pendingRelease()?.versionName)
            assertEquals(release.description, restored.pendingRelease()?.description)
            assertEquals(1, restoredStorage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
            assertEquals(0, downloads.requestCount) // The completed job was removed; no new download was enqueued.

            // An outdated ready notification may still be tapped after the app was upgraded.
            packageShadow.getInternalMutablePackageInfo(context.packageName).setLongVersionCode(10422)
            restoredStorage.notifications.filterIsInstance<InAppNotification.SelfUpdateReady>().last().action()
            runCurrent()
            assertTrue(restoredStorage.notifications.none { it is InAppNotification.SelfUpdateError })
            assertTrue(restoredStorage.notifications.contains(InAppNotification.SelfUpdateCleared(release.version)))
            assertEquals(-1, prefs.getLong("id", -1))
            assertEquals(null, shadowOf(activity).peekNextStartedActivity())

            // Recreate the previous pending state for the corrupted-file recovery check.
            packageShadow.getInternalMutablePackageInfo(context.packageName).setLongVersionCode(10421)
            prefs.edit().putLong("id", id).putString("version", release.version).putString("name", release.name)
                .putString("description", release.description).putLong("size", bytes.size.toLong())
                .putString("sha256", digest).putBoolean("ready", true).commit()

            // A changed staged file must never be advertised as ready after process recreation.
            verified.writeBytes(byteArrayOf(9, 9, 9, 9))
            val corruptStorage = Storage()
            assertIs<SelfUpdateResult.Error>(source(context, corruptStorage, release.copy(sha256 = digest)).resumeUpdate())
            assertTrue(corruptStorage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertTrue(corruptStorage.notifications.any { it is InAppNotification.SelfUpdateError })
            assertEquals(-1, prefs.getLong("id", -1))

            // Completion is reconciled against the installed build, not the installer launch.
            prefs.edit().putLong("id", id).putString("version", release.version).putString("name", release.name)
                .putString("description", release.description).putLong("size", bytes.size.toLong())
                .putString("sha256", digest).putBoolean("ready", true).commit()
            verified.writeBytes(bytes)
            packageShadow.getInternalMutablePackageInfo(context.packageName).setLongVersionCode(10422)
            val installedStorage = Storage()
            assertIs<SelfUpdateResult.NoUpdates>(source(context, installedStorage, release.copy(sha256 = digest)).resumeUpdate())
            assertEquals(-1, prefs.getLong("id", -1))
            assertTrue(!verified.exists())
            assertTrue(installedStorage.notifications.none { it is InAppNotification.SelfUpdateReady })
            assertTrue(installedStorage.notifications.contains(InAppNotification.SelfUpdateCleared(release.version)))
        } finally {
            CurrentActivityHolder.onActivityPaused(activity)
            activityController.pause().stop().destroy()
            Dispatchers.resetMain()
        }
    }
}

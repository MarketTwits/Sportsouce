package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.nio.file.Paths
import java.nio.file.Files
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SelfUpdaterDesktopTest {
    @Test
    fun retryAfterNewerDownloadFailsDoesNotGetStuckBehindAnOlderPreparedInstaller() = runTest {
        var published = release
        val newer = release.copy(version = "1.6.2.10423", name = "newer.dmg")
        val parser = object : DesktopReleaseParserApi {
            override suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture) = published
        }
        val updater = SelfUpdaterDesktop(parser, Storage(), { environment }, { _, _ -> error("offline") }, {},
            { item -> if (item == release) Paths.get("/tmp/updates/app.dmg") else null }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(false))
        published = newer
        assertIs<SelfUpdateResult.Error>(updater.checkUpdate(true))
        assertEquals(newer.version, assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(false)).releaseId)
    }

    @Test
    fun downloadedFileCanBeOpenedDirectlyFromTheUpdateScreen() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var valid = true
        val opened = mutableListOf<java.nio.file.Path>()
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, _ -> file },
            { opened.add(it) }, { if (valid) file else null }, this)
        assertEquals(com.markettwits.selfupdater.models.UpdateTarget.Desktop, updater.updateTarget())
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        assertEquals(release.version, updater.pendingRelease()?.versionName)
        assertEquals(release.description, updater.pendingRelease()?.description)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.installUpdate())
        assertEquals(listOf(file), opened)
        valid = false
        assertIs<SelfUpdateResult.Error>(updater.installUpdate())
        assertEquals(listOf(file), opened)
    }

    @Test
    fun closingScreenDoesNotCancelTransferAndProgressClearsAfterCompletion() = runTest {
        val started = CompletableDeferred<Unit>()
        val finish = CompletableDeferred<Unit>()
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var downloaded = false
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, progress ->
            progress(1, 2)
            started.complete(Unit)
            finish.await()
            downloaded = true
            file
        }, {}, { if (downloaded) file else null }, this)
        val job = async { updater.checkUpdate(true) }
        started.await()
        assertEquals(50, updater.downloadProgress().first())
        job.cancel()
        job.join()
        assertEquals(50, updater.downloadProgress().first())
        finish.complete(Unit)
        runCurrent()
        assertEquals(null, updater.downloadProgress().first())
        storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single()
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.resumeUpdate())
        assertEquals(1, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
    }

    @Test
    fun backgroundFailureAfterClosingScreenIsReported() = runTest {
        val started = CompletableDeferred<Unit>()
        val finish = CompletableDeferred<Unit>()
        val storage = Storage()
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, _ ->
            started.complete(Unit)
            finish.await()
            throw java.io.IOException("network failed")
        }, {}, { null }, this)
        val job = async { updater.checkUpdate(true) }
        started.await()
        job.cancel()
        job.join()
        finish.complete(Unit)
        runCurrent()
        storage.items.filterIsInstance<InAppNotification.SelfUpdateError>().single()
        assertEquals(null, updater.downloadProgress().first())
        assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
        assertIs<SelfUpdateResult.SelfUpdateReady>(updater.checkUpdate(false))
        assertEquals(SelfUpdateResult.NoUpdates, updater.resumeUpdate())
    }
    private val release = SelfUpdate("1.6.1.10422", "https://example.com", "app.dmg", "notes", 3, "a".repeat(64))
    private val environment = DesktopEnvironment(DesktopPlatform.MacOS, DesktopArchitecture.Arm64, 10421)
    private class Storage : InAppNotificationStorage {
        val items = mutableListOf<InAppNotification>()
        override fun subscribe(listener: InAppNotificationListener) = Unit
        override fun unsubscribe() = Unit
        override fun addNotification(notification: InAppNotification) { items += notification }
    }
    private fun parser(value: SelfUpdate?) = object : DesktopReleaseParserApi {
        override suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture): SelfUpdate? {
            assertEquals(DesktopPlatform.MacOS, platform)
            assertEquals(DesktopArchitecture.Arm64, architecture)
            return value
        }
    }

    @Test
    fun checksVersionWithoutDownloadingAndUsesDistinctUnsupportedState() = runTest {
        val storage = Storage()
        var downloads = 0
        fun updater(value: SelfUpdate?, env: DesktopEnvironment? = environment) = SelfUpdaterDesktop(
            parser(value), storage, { env }, { _, _ -> downloads++; Paths.get("/tmp/app.dmg") }, {},
        )
        val available = assertIs<SelfUpdateResult.SelfUpdateReady>(updater(release).checkUpdate(false))
        assertEquals("1.6.1", available.versionName)
        assertEquals(0, downloads)
        assertEquals(SelfUpdateResult.NoUpdates, updater(release, environment.copy(buildNumber = 10422)).checkUpdate(false))
        assertEquals(SelfUpdateResult.Unsupported, updater(null).checkUpdate(false))
        assertEquals(SelfUpdateResult.Unsupported, updater(release, null).checkUpdate(false))
    }

    @Test
    fun manualDownloadOffersInstallerWithoutOpeningItAutomatically() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var opened: java.nio.file.Path? = null
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, _ -> file }, { opened = it }, { file }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        assertNull(opened)
        val notification = storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single()
        assertEquals(true, notification.desktopInstaller)
        notification.action()
        runCurrent()
        assertEquals(file, opened)
    }

    @Test
    fun supersededNotificationCannotOpenAnOlderInstaller() = runTest {
        val storage = Storage()
        val newer = release.copy(version = "1.6.1.10423", name = "newer.dmg")
        var currentRelease = release
        val parser = object : DesktopReleaseParserApi {
            override suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture) = currentRelease
        }
        val opened = mutableListOf<java.nio.file.Path>()
        val updater = SelfUpdaterDesktop(parser, storage, { environment },
            { item, _ -> Paths.get("/tmp/updates", item.name) }, { opened.add(it) },
            { item -> Paths.get("/tmp/updates", item.name) }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        val oldNotification = storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single()
        currentRelease = newer
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        val newNotification = assertIs<InAppNotification.SelfUpdateReady>(storage.items.last())
        oldNotification.action()
        runCurrent()
        assertEquals(emptyList(), opened)
        assertIs<InAppNotification.SelfUpdateError>(storage.items.last())
        newNotification.action()
        runCurrent()
        assertEquals(listOf(Paths.get("/tmp/updates", newer.name)), opened)
    }

    @Test
    fun installerCannotOpenAfterApplicationWasUpdated() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var currentEnvironment = environment
        var opened = false
        val updater = SelfUpdaterDesktop(parser(release), storage, { currentEnvironment },
            { _, _ -> file }, { opened = true }, { file }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        currentEnvironment = environment.copy(buildNumber = 10422)
        storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single().action()
        runCurrent()
        assertEquals(false, opened)
        assertIs<InAppNotification.SelfUpdateError>(storage.items.last())
    }

    @Test
    fun previouslyVerifiedDownloadIsOfferedAgainWithoutDownloading() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var downloads = 0
        var opened: java.nio.file.Path? = null
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, _ -> downloads++; file }, { opened = it }, { file }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(false))
        assertEquals(0, downloads)
        assertNull(opened)
        storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single().action()
        runCurrent()
        assertEquals(file, opened)
    }

    @Test
    fun changedInstallerIsNeverOpened() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var valid = true
        var opened = false
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, _ -> file }, { opened = true }, { if (valid) file else null }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        valid = false
        storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single().action()
        runCurrent()
        assertEquals(false, opened)
        assertIs<InAppNotification.SelfUpdateError>(storage.items.last())
    }

    @Test
    fun dismissedInstallerIsRestoredOfflineWithoutDuplicateNotificationsOrDownloads() = runTest {
        val storage = Storage()
        val file = Paths.get("/tmp/updates/app.dmg")
        var requests = 0
        var downloads = 0
        var valid = false
        val parser = object : DesktopReleaseParserApi {
            override suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture): SelfUpdate {
                requests++
                return release
            }
        }
        val updater = SelfUpdaterDesktop(parser, storage, { environment }, { _, _ -> downloads++; valid = true; file }, {}, { if (valid) file else null }, this)
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.checkUpdate(true))
        val initial = storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single()
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.resumeUpdate())
        assertEquals(1, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
        initial.onDismiss()
        assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, updater.resumeUpdate())
        assertEquals(2, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
        assertEquals(1, downloads)
        assertEquals(1, requests)
        valid = false
        assertIs<SelfUpdateResult.Error>(updater.resumeUpdate())
        assertEquals(2, storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().size)
        assertIs<InAppNotification.SelfUpdateCleared>(storage.items.last())
    }

    @Test
    fun restartRecoversDownloadedInstallerUsingReleaseMetadata() = runTest {
        val directory = Files.createTempDirectory("sportsauce-desktop-restart")
        try {
            val bytes = "installer".toByteArray()
            val hash = MessageDigest.getInstance("SHA-256").digest(bytes)
                .joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') }
            val release = release.copy(sizeBytes = bytes.size.toLong(), sha256 = hash)
            val request = DesktopDownloadRequest(release.name, bytes.size.toLong(), hash)
            val downloader = DesktopUpdateDownloader(directory)
            var downloads = 0
            fun create(storage: Storage) = SelfUpdaterDesktop(
                parser(release), storage, { environment },
                { _, _ -> downloads++; downloader.download(request, { bytes.inputStream() }) }, {},
                { downloader.findVerified(request) },
            )
            assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, create(Storage()).checkUpdate(true))
            val afterRestart = Storage()
            assertEquals(SelfUpdateResult.DesktopInstallerDownloaded, create(afterRestart).checkUpdate(false))
            assertEquals(1, downloads)
            assertIs<InAppNotification.SelfUpdateReady>(afterRestart.items.single())
            Files.write(directory.resolve(release.name), "corrupt".toByteArray())
            assertIs<SelfUpdateResult.SelfUpdateReady>(create(Storage()).checkUpdate(false))
        } finally { directory.toFile().deleteRecursively() }
    }

    @Test
    fun rejectsParallelDownloadsAndPropagatesCancellation() = runTest {
        val storage = Storage()
        val started = CompletableDeferred<Unit>()
        val releaseDownload = CompletableDeferred<Unit>()
        val updater = SelfUpdaterDesktop(parser(release), storage, { environment }, { _, progress ->
            progress(50, 100)
            started.complete(Unit)
            releaseDownload.await()
            Paths.get("/tmp/updates/app.dmg")
        }, {}, { null }, this)
        val first = async { updater.checkUpdate(true) }
        started.await()
        assertEquals(SelfUpdateResult.DownloadProgress(50, false), updater.checkUpdate(true))
        first.cancel()
        first.join()
        assertEquals(SelfUpdateResult.DownloadProgress(50, false), updater.resumeUpdate())
        assertEquals(SelfUpdateResult.DownloadProgress(50, false), updater.checkUpdate(false))
        assertEquals(true, storage.items.none { it is InAppNotification.SelfUpdateReady })
        releaseDownload.complete(Unit)
        runCurrent()
        storage.items.filterIsInstance<InAppNotification.SelfUpdateReady>().single()
    }

    @Test
    fun packagedLauncherMustMatchTheRunningProcess() {
        val directory = Files.createTempDirectory("sportsauce-packaged-launcher")
        try {
            val launcher = Files.writeString(directory.resolve("SportSauce"), "launcher")
            val other = Files.writeString(directory.resolve("java"), "other")
            assertEquals(true, isPackagedDesktop(launcher.toString(), launcher.toString()))
            assertEquals(false, isPackagedDesktop(null, launcher.toString()))
            assertEquals(false, isPackagedDesktop(launcher.toString(), other.toString()))
            assertEquals(false, isPackagedDesktop(directory.resolve("missing").toString(), launcher.toString()))
        } finally { directory.toFile().deleteRecursively() }
    }

    @Test
    fun mapsOperatingSystemAndCpuWithoutGuessing() {
        assertEquals(environment, desktopEnvironment("Mac OS X", "aarch64", 10421))
        assertEquals(DesktopArchitecture.X64, desktopEnvironment("Windows 11", "amd64", 1)?.architecture)
        assertNull(desktopEnvironment("Linux", "x86_64", 1))
        assertNull(desktopEnvironment("Windows", "x86", 1))
        assertNull(desktopEnvironment("Mac OS X", "arm64", 0))
    }
}

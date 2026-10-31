package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.Desktop
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicBoolean

internal data class DesktopEnvironment(val platform: DesktopPlatform, val architecture: DesktopArchitecture, val buildNumber: Long)

internal fun isPackagedDesktop(launcher: String?, processCommand: String?): Boolean {
    if (launcher.isNullOrBlank() || processCommand.isNullOrBlank()) return false
    return try {
        val expected = Paths.get(launcher).toRealPath()
        val running = Paths.get(processCommand).toRealPath()
        Files.isRegularFile(expected) && expected == running
    } catch (_: Exception) {
        false
    }
}

internal fun desktopEnvironment(os: String, architecture: String, buildNumber: Long): DesktopEnvironment? {
    val platform = when {
        os.lowercase().startsWith("windows") -> DesktopPlatform.Windows
        os.lowercase().startsWith("mac") || os.lowercase().startsWith("darwin") -> DesktopPlatform.MacOS
        else -> return null
    }
    val cpu = when (architecture.lowercase()) {
        "amd64", "x86_64" -> DesktopArchitecture.X64
        "aarch64", "arm64" -> DesktopArchitecture.Arm64
        else -> return null
    }
    return DesktopEnvironment(platform, cpu, buildNumber.takeIf { it > 0 } ?: return null)
}

class SelfUpdaterDesktop internal constructor(
    private val parser: DesktopReleaseParserApi,
    private val notifications: InAppNotificationStorage,
    private val environment: () -> DesktopEnvironment?,
    private val download: suspend (SelfUpdate, (Long, Long) -> Unit) -> Path,
    private val openInstaller: (Path) -> Unit,
    private val findDownloaded: suspend (SelfUpdate) -> Path? = { null },
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : SelfUpdaterSourceApi {
    private data class ReadyOffer(
        val release: SelfUpdate,
        val file: Path,
        val version: String,
        val diskImage: Boolean,
        val notificationVisible: AtomicBoolean = AtomicBoolean(false),
    )
    @Volatile private var readyOffer: ReadyOffer? = null
    @Volatile private var lastTransferFailure: SelfUpdateResult.Error? = null
    private val transferProgress = MutableStateFlow<Int?>(null)
    @Volatile private var transferRelease: SelfUpdate? = null
    constructor(parser: DesktopReleaseParserApi, notifications: InAppNotificationStorage) : this(
        parser = parser,
        notifications = notifications,
        environment = {
            if (!isPackagedDesktop(System.getProperty("jpackage.app-path"), ProcessHandle.current().info().command().orElse(null))) null
            else desktopEnvironment(System.getProperty("os.name", ""), System.getProperty("os.arch", ""), BuildKonfig.APP_VERSION_NUMBER.toLong())
        },
        download = { release, progress ->
            val request = DesktopDownloadRequest(release.name, requireNotNull(release.sizeBytes), requireNotNull(release.sha256))
            val directory = Paths.get(System.getProperty("user.home"), ".sportsauce", "updates")
            DesktopUpdateDownloader(directory).download(request, GithubDesktopDownloadSource(release.downloadUrl), progress)
        },
        openInstaller = { path -> Desktop.getDesktop().open(path.toFile()) },
        findDownloaded = { release ->
            val request = DesktopDownloadRequest(release.name, requireNotNull(release.sizeBytes), requireNotNull(release.sha256))
            val directory = Paths.get(System.getProperty("user.home"), ".sportsauce", "updates")
            DesktopUpdateDownloader(directory).findVerified(request)
        },
    )

    override fun updateTarget() = UpdateTarget.Desktop
    override fun pendingRelease(): SelfUpdateResult.SelfUpdateReady? =
        (transferRelease ?: readyOffer?.release)?.let {
            SelfUpdateResult.SelfUpdateReady(it.version, it.description, releaseId = it.version)
        }

    override suspend fun installUpdate(): SelfUpdateResult {
        val offer = readyOffer ?: return SelfUpdateResult.NoUpdates
        return open(offer)
    }

    override fun getInstallSourceName(): String = "GitHub"
    override fun isSelfUpdateCanManualCheck(): Boolean = environment() != null
    override fun downloadProgress() = transferProgress.asStateFlow()

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        transferProgress.value?.let { return SelfUpdateResult.DownloadProgress(it, false) }
        val retryingFailure = lastTransferFailure != null
        lastTransferFailure = null
        if (!manual && !retryingFailure && readyOffer != null) {
            val restored = resumeUpdate()
            if (restored !is SelfUpdateResult.Error) return restored
        }
        val target = environment() ?: return SelfUpdateResult.Unsupported
        val release = try {
            parser.getLatestDesktopUpdate(target.platform, target.architecture) ?: return SelfUpdateResult.Unsupported
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            return SelfUpdateResult.Error(IllegalStateException("Unable to check desktop updates", failure))
        }
        val version = ReleaseVersion.fromTag(release.version)
            ?: return SelfUpdateResult.Error(IllegalStateException("Invalid desktop release version"))
        if (version.buildNumber <= target.buildNumber) return SelfUpdateResult.NoUpdates
        if (!mutex.tryLock()) return transferProgress.value?.let { SelfUpdateResult.DownloadProgress(it, false) }
            ?: SelfUpdateResult.DownloadInProgress
        lastTransferFailure = null
        // The source owns the operation. A screen disappearing only cancels its await,
        // not the download or the eventual ready/error notification.
        val operation = scope.async(start = CoroutineStart.UNDISPATCHED) {
            try {
                try {
                    val file = findDownloaded(release) ?: if (manual) {
                        transferRelease = release
                        transferProgress.value = 0
                        notifications.addNotification(InAppNotification.SelfUpdateStarted)
                        download(release) { bytes, total ->
                            if (total > 0) {
                                val percent = ((bytes.coerceIn(0, total).toDouble() / total) * 100).toInt().coerceIn(0, 99)
                                if (transferProgress.value != percent) {
                                    transferProgress.value = percent
                                    notifications.addNotification(InAppNotification.SelfUpdateProgress(percent))
                                }
                            }
                        }
                    } else {
                        return@async SelfUpdateResult.SelfUpdateReady(version.versionName, release.description, releaseId = release.version)
                    }
                    val offer = readyOffer?.takeIf { it.release == release && it.file == file }
                        ?: ReadyOffer(release, file, version.versionName, target.platform == DesktopPlatform.MacOS).also { readyOffer = it }
                    present(offer)
                    SelfUpdateResult.DesktopInstallerDownloaded
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (failure: Exception) {
                    val error = IllegalStateException("Unable to download desktop update", failure)
                    if (manual) lastTransferFailure = SelfUpdateResult.Error(error)
                    if (manual) notifications.addNotification(InAppNotification.SelfUpdateError(error))
                    SelfUpdateResult.Error(error)
                }
            } finally {
                transferProgress.value = null
                transferRelease = null
                mutex.unlock()
            }
        }
        return operation.await()
    }

    override suspend fun resumeUpdate(): SelfUpdateResult {
        transferProgress.value?.let { return SelfUpdateResult.DownloadProgress(it, false) }
        lastTransferFailure?.let { return it }
        val offer = readyOffer ?: return SelfUpdateResult.NoUpdates
        if (!mutex.tryLock()) return SelfUpdateResult.InProgress
        return try {
            val target = environment() ?: return SelfUpdateResult.Unsupported
            val version = ReleaseVersion.fromTag(offer.release.version) ?: return SelfUpdateResult.Unsupported
            if (target.buildNumber >= version.buildNumber) {
                readyOffer = null
                notifications.addNotification(InAppNotification.SelfUpdateCleared(offer.release.version))
                return SelfUpdateResult.NoUpdates
            }
            if (findDownloaded(offer.release) != offer.file) {
                readyOffer = null
                notifications.addNotification(InAppNotification.SelfUpdateCleared(offer.release.version))
                return SelfUpdateResult.Error(IllegalStateException("Downloaded installer is missing or changed"))
            }
            present(offer)
            SelfUpdateResult.DesktopInstallerDownloaded
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            SelfUpdateResult.Error(IllegalStateException("Unable to restore desktop update", failure))
        } finally {
            mutex.unlock()
        }
    }

    private fun present(offer: ReadyOffer) {
        if (!offer.notificationVisible.compareAndSet(false, true)) return
        notifications.addNotification(InAppNotification.SelfUpdateReady(
            action = {
                offer.notificationVisible.set(false)
                scope.launch { open(offer) }
            },
            actualVersion = offer.version,
            description = offer.release.description,
            readyToInstall = true,
            releaseId = offer.release.version,
            desktopInstaller = true,
            desktopDiskImage = offer.diskImage,
            onDismiss = { offer.notificationVisible.set(false) },
        ))
    }

    private suspend fun open(offer: ReadyOffer): SelfUpdateResult = try {
        mutex.withLock {
            check(readyOffer === offer) { "This update has been superseded" }
            val target = requireNotNull(environment()) { "Desktop environment is unsupported" }
            val version = requireNotNull(ReleaseVersion.fromTag(offer.release.version)) { "Invalid update version" }
            check(target.buildNumber < version.buildNumber) { "This update is no longer newer than the installed version" }
            val verified = requireNotNull(findDownloaded(offer.release)) { "Downloaded installer is missing or changed" }
            check(verified == offer.file) { "Downloaded installer is missing or changed" }
            openInstaller(verified)
            offer.notificationVisible.set(false)
            SelfUpdateResult.DesktopInstallerDownloaded
        }
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (failure: Exception) {
        notifications.addNotification(InAppNotification.SelfUpdateError(failure))
        SelfUpdateResult.Error(failure)
    }

    private companion object { val mutex = Mutex() }
}

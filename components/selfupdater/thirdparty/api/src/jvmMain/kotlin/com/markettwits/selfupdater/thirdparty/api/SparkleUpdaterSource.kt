package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex

/** A native bridge supplies these results after verifying and interpreting the signed feed. */
internal interface SparkleGateway {
    suspend fun discover(): SparkleDiscovery
    /** Starts the user-facing update flow, without claiming that installation succeeded. */
    suspend fun startInteractiveUpdate(): Boolean
    /** True only while Sparkle has an existing update session to show or restore. */
    suspend fun hasActiveSession(): Boolean
}

internal sealed interface SparkleDiscovery {
    data class Available(val versionName: String, val buildNumber: Long, val description: String) : SparkleDiscovery
    data object Current : SparkleDiscovery
    data object Unsupported : SparkleDiscovery
}

/**
 * Adapter for the existing facade. It is deliberately not wired into production until a
 * signed feed, native bridge and packaged installation/relaunch have been verified.
 */
internal class SparkleUpdaterSource(
    private val gateway: SparkleGateway,
    private val installedBuild: () -> Long,
) : SelfUpdaterSourceApi {
    private val mutex = Mutex()
    private var wasActive = false

    override fun getInstallSourceName() = "GitHub"
    override fun isSelfUpdateCanManualCheck() = true

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult = exclusive {
        if (wasActive && gateway.hasActiveSession()) {
            // A repeated explicit action may bring Sparkle's existing window forward.
            // A false response can also mean Sparkle is busy with background work.
            if (manual) gateway.startInteractiveUpdate()
            SelfUpdateResult.InteractiveUpdateInProgress
        } else {
            discover(manual).also {
                if (it != SelfUpdateResult.InteractiveUpdateInProgress) wasActive = false
            }
        }
    }

    private suspend fun discover(manual: Boolean): SelfUpdateResult {
        val currentBuild = installedBuild()
        require(currentBuild > 0) { "Installed build number is unavailable" }
        return when (val result = gateway.discover()) {
            SparkleDiscovery.Current -> SelfUpdateResult.NoUpdates
            SparkleDiscovery.Unsupported -> SelfUpdateResult.Unsupported
            is SparkleDiscovery.Available -> {
                val tag = "${result.versionName}.${result.buildNumber}"
                val version = requireNotNull(ReleaseVersion.fromTag(tag)) { "Invalid Sparkle release version" }
                require(version.versionName == result.versionName && version.buildNumber > currentBuild) {
                    "Sparkle release is not a valid upgrade"
                }
                if (!manual) {
                    SelfUpdateResult.SelfUpdateReady(version.versionName, result.description, releaseId = tag)
                } else {
                    // The native command may execute after its caller is cancelled or
                    // times out. Reconcile on resume even if no reply reaches Kotlin.
                    wasActive = true
                    check(gateway.startInteractiveUpdate()) { "Unable to start the macOS update" }
                    SelfUpdateResult.InteractiveUpdateInProgress
                }
            }
        }
    }

    override suspend fun resumeUpdate(): SelfUpdateResult = exclusive {
        if (gateway.hasActiveSession()) {
            wasActive = true
            SelfUpdateResult.InteractiveUpdateInProgress
        } else if (wasActive) {
            // A closed session can mean cancellation or failure; recheck instead of
            // reporting success merely because the native UI disappeared.
            discover(false).also { wasActive = false }
        } else SelfUpdateResult.NoUpdates
    }

    private suspend fun exclusive(operation: suspend () -> SelfUpdateResult): SelfUpdateResult {
        if (!mutex.tryLock()) return SelfUpdateResult.InProgress
        return try {
            operation()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            SelfUpdateResult.Error(failure)
        } finally {
            mutex.unlock()
        }
    }
}

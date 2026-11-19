package com.markettwits.selfupdater.googleplay.api

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.buildkonfig.isDebugMode
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val UPDATE_CODE = 228

class SelfUpdaterGooglePlay(
    context: Context,
    private val inAppNotificationStorage: InAppNotificationStorage,
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context),
) : SelfUpdaterSourceApi {
    private var downloadedNotificationSent = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val controller = GooglePlayUpdateController(object : PlayUpdateGateway<AppUpdateInfo> {
        override suspend fun getUpdateInfo(): PlayUpdateSnapshot<AppUpdateInfo> {
            val info = appUpdateManager.appUpdateInfo.await()
            return PlayUpdateSnapshot(
                handle = info,
                availability = when (info.updateAvailability()) {
                    UpdateAvailability.UPDATE_AVAILABLE -> PlayAvailability.Available
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> PlayAvailability.InProgress
                    UpdateAvailability.UPDATE_NOT_AVAILABLE -> PlayAvailability.NotAvailable
                    else -> PlayAvailability.Unknown
                },
                immediateAllowed = info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE),
                downloaded = info.installStatus() == InstallStatus.DOWNLOADED,
            )
        }

        override fun startImmediateUpdate(handle: AppUpdateInfo): Boolean {
            val activity = CurrentActivityHolder.getCurrentActivity()
                ?.takeUnless { it.isFinishing || it.isDestroyed }
                ?: error("A resumed activity is required to start the Google Play update")
            return appUpdateManager.startUpdateFlowForResult(handle, AppUpdateType.IMMEDIATE, activity, UPDATE_CODE)
        }
    })

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult = mapResult(controller.check())

    override suspend fun resumeUpdate(): SelfUpdateResult = mapResult(controller.resume())

    override suspend fun installUpdate(): SelfUpdateResult = try {
        val activity = CurrentActivityHolder.getCurrentActivity()
        check(activity != null && !activity.isFinishing && !activity.isDestroyed) { "A resumed activity is required" }
        appUpdateManager.completeUpdate().await()
        downloadedNotificationSent = false
        SelfUpdateResult.ReadyToInstall
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (failure: Exception) {
        SelfUpdateResult.Error(failure)
    }

    private fun mapResult(result: PlayCheckResult): SelfUpdateResult {
        if (result != PlayCheckResult.ReadyToComplete) downloadedNotificationSent = false
        return when (result) {
            PlayCheckResult.Started -> SelfUpdateResult.Complete
            PlayCheckResult.NoUpdate -> SelfUpdateResult.NoUpdates
            PlayCheckResult.Unavailable -> SelfUpdateResult.Error(IllegalStateException("Google Play update is unavailable"))
            PlayCheckResult.ReadyToComplete -> {
                notifyDownloaded()
                SelfUpdateResult.ReadyToInstall
            }
        }
    }

    private fun notifyDownloaded() {
        if (downloadedNotificationSent) return
        downloadedNotificationSent = true
        // Play does not expose a version name for the downloaded update.
        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateReady(
            action = {
                scope.launch {
                    try {
                        val activity = CurrentActivityHolder.getCurrentActivity()
                        check(activity != null && !activity.isFinishing && !activity.isDestroyed) { "A resumed activity is required" }
                        appUpdateManager.completeUpdate().await()
                        // If Play returns without installation, the next foreground check can offer it again.
                        downloadedNotificationSent = false
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (failure: Exception) {
                        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateError(failure))
                        downloadedNotificationSent = false
                        notifyDownloaded()
                    }
                }
            },
            actualVersion = "",
            description = "",
            readyToInstall = true,
            onDismiss = { downloadedNotificationSent = false },
        ))
    }

    override fun getInstallSourceName() = "Google Play/" + if (isDebugMode) "debug" else "release"

    override fun isSelfUpdateCanManualCheck(): Boolean = false
}

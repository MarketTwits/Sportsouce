package com.flipperdevices.selfupdater.googleplay.api

import android.content.Context
import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.inappnotification.api.BuildConfig
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.markettwits.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.tasks.await

private const val UPDATE_CODE = 228

class SelfUpdaterGooglePlay(
    private val context: Context,
    private val inAppNotificationStorage: InAppNotificationStorage
) : SelfUpdaterSourceApi {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(context) }

    private val appUpdateInfoTask by lazy { appUpdateManager.appUpdateInfo }

    private var updateListener = InstallStateUpdatedListener(::processUpdateListener)

    private fun processUpdateListener(state: InstallState) {
        val installStatus = state.installStatus()

        when (installStatus) {
            InstallStatus.DOWNLOADED -> {
                val notification = InAppNotification.SelfUpdateReady(
                    action = appUpdateManager::completeUpdate,
                    actualVersion = BuildConfig.LIBRARY_PACKAGE_NAME,
                    description = ""
                )
                inAppNotificationStorage.addNotification(notification)
            }

            InstallStatus.FAILED, InstallStatus.CANCELED -> {
                val notification = InAppNotification.SelfUpdateError(Exception())
                inAppNotificationStorage.addNotification(notification)
            }

            else -> {}
        }
    }

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        val activity = CurrentActivityHolder.getCurrentActivity()
            ?: return SelfUpdateResult.Error(NoSuchElementException("activity not found"))

        appUpdateManager.registerListener(updateListener)

        val appUpdateInfo = appUpdateInfoTask.await()

        return if (isUpdateAvailable(appUpdateInfo)) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                activity,
                UPDATE_CODE
            )
            SelfUpdateResult.Complete
        } else {
            SelfUpdateResult.NoUpdates
        }
    }

    override fun getInstallSourceName() = "Google Play/" + BuildConfig.BUILD_TYPE

    override fun isSelfUpdateCanManualCheck(): Boolean = false

    private fun isUpdateAvailable(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
    }
}

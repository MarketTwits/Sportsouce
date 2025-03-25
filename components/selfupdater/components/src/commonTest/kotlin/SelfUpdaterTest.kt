import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

class SelfUpdaterTest(
    private val result: SelfUpdateResult,
    private val inAppNotificationStorage: InAppNotificationStorage
) : SelfUpdaterSourceApi {
    private var isManual = false
    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        isManual = manual
        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateStarted)
        return result
    }

    override fun getInstallSourceName(): String = "sportSauce"

    override fun isSelfUpdateCanManualCheck(): Boolean {
        inAppNotificationStorage.addNotification(InAppNotification.SelfUpdateStarted)
        return isManual
    }
}
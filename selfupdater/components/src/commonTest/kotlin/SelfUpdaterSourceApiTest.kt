import com.flipperdevices.inappnotification.impl.storage.InAppNotificationStorageImpl
import com.flipperdevices.selfupdater.impl.api.SelfUpdaterApiImpl
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.markettwits.inappnotification.api.InAppNotificationListener
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.markettwits.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration


class SelfUpdaterSourceApiTest {
    private val inAppNotificationStorage = InAppNotificationStorageImpl()
    private val selfUpdater = SelfUpdaterApiImpl(
        SelfUpdaterTest(
            SelfUpdateResult.InProgress,
            inAppNotificationStorage
        ),
    )
    private val notificationComponent = InAppNotificationComponentMock(inAppNotificationStorage)

    @Test
    fun check_start_check_update_manual() = runTest(timeout = Duration.ZERO) {
        selfUpdater.startCheckUpdate(true)
        val manual = selfUpdater.isSelfUpdateCanManualCheck()
        assertEquals(manual, true)
    }

    @Test
    fun check_start_check_update_not_manual() = runTest(timeout = Duration.ZERO) {
        selfUpdater.startCheckUpdate(false)
        val manual = selfUpdater.isSelfUpdateCanManualCheck()
        assertEquals(manual, false)
    }

    @Test
    fun check_notification_started() = runTest(timeout = Duration.ZERO) {
        selfUpdater.startCheckUpdate(false)
        assertEquals(InAppNotification.SelfUpdateStarted, notificationComponent.innerNotification)
    }

    @Test
    fun check_notification_updated() = runTest(timeout = Duration.ZERO) {
        selfUpdater.isSelfUpdateCanManualCheck()
        assertEquals(InAppNotification.SelfUpdateStarted, notificationComponent.innerNotification)
    }

    @Test
    fun check_notification_unsubscribe() = runTest(timeout = Duration.ZERO) {
        notificationComponent.unsubscribe()
        selfUpdater.isSelfUpdateCanManualCheck()
        assertEquals(InAppNotification.HiddenApp({}), notificationComponent.innerNotification)
    }
}

class InAppNotificationComponentMock(
    private val notificationStorage: InAppNotificationStorage,
) : InAppNotificationListener {
    var innerNotification: InAppNotification = InAppNotification.HiddenApp({})

    fun unsubscribe() {
        notificationStorage.unsubscribe()
    }

    init {
        notificationStorage.subscribe(this@InAppNotificationComponentMock)
    }

    override suspend fun onNewNotification(notification: InAppNotification) {
        innerNotification = notification
    }
}
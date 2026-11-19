import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.impl.api.SelfUpdaterApiImpl
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.storage.InAppNotificationStorageImpl
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SelfUpdaterSourceApiTest {
    @Test
    fun notificationsAreDeliveredInOrderAndRetainedWhileUnsubscribed() = runTest {
        val storage = InAppNotificationStorageImpl(backgroundScope)
        val received = mutableListOf<InAppNotification>()
        val listener = object : InAppNotificationListener {
            override suspend fun onNewNotification(notification: InAppNotification) {
                received += notification
            }
        }

        storage.addNotification(InAppNotification.SelfUpdateStarted)
        storage.subscribe(listener)
        runCurrent()
        assertEquals(listOf<InAppNotification>(InAppNotification.SelfUpdateStarted), received)

        storage.unsubscribe()
        storage.addNotification(InAppNotification.SelfUpdateStarted)
        runCurrent()
        assertEquals(1, received.size)
        storage.subscribe(listener)
        runCurrent()
        assertEquals(2, received.size)
    }

    @Test
    fun concurrentChecksAreRejectedAndGuardIsReleasedOnCancellation() = runTest {
        val started = CompletableDeferred<Unit>()
        val neverComplete = CompletableDeferred<Unit>()
        val source = object : SelfUpdaterSourceApi {
            override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
                started.complete(Unit)
                neverComplete.await()
                return SelfUpdateResult.NoUpdates
            }
            override fun getInstallSourceName() = "test"
            override fun isSelfUpdateCanManualCheck() = true
        }
        val updater = SelfUpdaterApiImpl(source)
        val first = launch { updater.startCheckUpdate() }
        started.await()
        assertTrue(updater.getInProgressState().value)
        assertIs<SelfUpdateResult.InProgress>(updater.startCheckUpdate())
        first.cancelAndJoin()
        assertFalse(updater.getInProgressState().value)
    }

    @Test
    fun manualFlagIsPassedToSourceAndErrorsReleaseGuard() = runTest {
        var observedManual = false
        var fail = false
        val source = object : SelfUpdaterSourceApi {
            override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
                observedManual = manual
                if (fail) error("failed")
                return SelfUpdateResult.NoUpdates
            }
            override fun getInstallSourceName() = "test"
            override fun isSelfUpdateCanManualCheck() = true
        }
        val updater = SelfUpdaterApiImpl(source)
        assertEquals(SelfUpdateResult.NoUpdates, updater.startCheckUpdate(true))
        assertTrue(observedManual)
        fail = true
        assertIs<SelfUpdateResult.Error>(updater.startCheckUpdate(false))
        assertFalse(observedManual)
        assertFalse(updater.getInProgressState().value)
    }
}

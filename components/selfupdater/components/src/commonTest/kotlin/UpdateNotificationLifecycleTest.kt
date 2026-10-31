import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponentBase
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.storage.InAppNotificationStorageImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.hours
import kotlin.time.TestTimeSource

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateNotificationLifecycleTest {
    @Test
    fun webKeepsOnlyTheLatestDeploymentAndClearsItAfterAConfirmedRollback() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            var result: SelfUpdateResult = SelfUpdateResult.SelfUpdateReady(
                "1.6.1", "first deployment", informationalOnly = true, releaseId = "web:12")
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = result
                override suspend fun resumeUpdate() = result
                override fun updateTarget() = com.markettwits.selfupdater.models.UpdateTarget.Web
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "Веб-версия"
                override fun isSelfUpdateCanManualCheck() = false
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {})
            lifecycle.resume(); runCurrent()
            val first = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            result = SelfUpdateResult.Error(IllegalStateException("offline"))
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(first, component.state.value, "A failed check must retain the last confirmed offer")
            result = SelfUpdateResult.SelfUpdateReady("1.6.2", "latest deployment", informationalOnly = true, releaseId = "web:13")
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            val latest = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            assertEquals("web:13", assertIs<InAppNotification.SelfUpdateReady>(latest.notification).releaseId)
            assertEquals(emptyList(), latest.pending, "Old site deployments must not block the current one")
            result = SelfUpdateResult.NoUpdates
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(InAppNotificationStore.State.NotPresent, component.state.value,
                "A confirmed rollback must remove the obsolete site update")
            result = SelfUpdateResult.SelfUpdateReady("1.6.3", "next deployment", informationalOnly = true, releaseId = "web:14")
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals("web:14", assertIs<InAppNotification.SelfUpdateReady>(
                assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification).releaseId)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun retryReplacesFailuresWithProgressInsteadOfQueueingBehindAnOldError() {
        with(com.markettwits.selfupdater.components.notification.store.InAppNotificationReducer) {
            val initial: InAppNotificationStore.State = InAppNotificationStore.State.NotPresent
            val failed = initial.reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateError(IllegalStateException("offline"))))
                .reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateError(IllegalStateException("offline again"))))
            assertEquals(emptyList(), assertIs<InAppNotificationStore.State.ShownNotification>(failed).pending)
            val progress = failed.reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateStarted))
                .reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateProgress(42)))
            assertEquals(InAppNotification.SelfUpdateProgress(42), assertIs<InAppNotificationStore.State.ShownNotification>(progress).notification)
            assertEquals(emptyList(), progress.pending)
        }
    }

    @Test
    fun newWebDeploymentIsOfferedWhenReturningToTheTabWithoutWaitingADay() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            var published = false
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.NoUpdates
                override suspend fun resumeUpdate() = if (published)
                    SelfUpdateResult.SelfUpdateReady("1.6.1", "new site", informationalOnly = true, releaseId = "web:12")
                    else SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "Веб-версия"
                override fun isSelfUpdateCanManualCheck() = false
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {})
            lifecycle.resume(); runCurrent()
            assertEquals(InAppNotificationStore.State.NotPresent, component.state.value)
            lifecycle.pause(); published = true; lifecycle.resume(); runCurrent()
            val offer = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            assertEquals("web:12", assertIs<InAppNotification.SelfUpdateReady>(offer.notification).releaseId)
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(emptyList(), assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).pending)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun clearedReleaseRemovesItsOffersWithoutRemovingOtherNotifications() {
        val reducer = com.markettwits.selfupdater.components.notification.store.InAppNotificationReducer
        val ready = InAppNotification.SelfUpdateReady({}, "1.6.1", "", readyToInstall = true, releaseId = "1.6.1.10422")
        val other = InAppNotification.SelfUpdateReady({}, "1.6.2", "", releaseId = "1.6.2.10423")
        val error = InAppNotification.SelfUpdateError(IllegalStateException("offline"))
        with(reducer) {
            val initial: InAppNotificationStore.State = InAppNotificationStore.State.NotPresent
            assertEquals(initial, initial.reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateCleared("1.6.1.10422"))))
            val showing = initial.reduce(InAppNotificationStore.Message.Enqueue(ready))
                .reduce(InAppNotificationStore.Message.Enqueue(error))
                .reduce(InAppNotificationStore.Message.Enqueue(other))
            val cleared = showing.reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateCleared("1.6.1.10422")))
            assertEquals(error, assertIs<InAppNotificationStore.State.ShownNotification>(cleared).notification)
            assertEquals(listOf(other), cleared.pending)
            val queued = initial.reduce(InAppNotificationStore.Message.Enqueue(error))
                .reduce(InAppNotificationStore.Message.Enqueue(ready))
                .reduce(InAppNotificationStore.Message.Enqueue(other))
                .reduce(InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateCleared("1.6.1.10422")))
            // A ready installer supersedes the previous updater failure.
            assertEquals(other, assertIs<InAppNotificationStore.State.ShownNotification>(queued).notification)
            assertEquals(emptyList(), queued.pending)
        }
    }
    @Test
    fun initialCheckRetriesWhenAnotherUpdateOperationIsRunning() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val running = MutableStateFlow(true)
            var checks = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return if (running.value) SelfUpdateResult.InProgress
                    else SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = "1.6.1.10422")
                }
                override fun getInProgressState() = running
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {})
            lifecycle.resume()
            runCurrent()
            assertEquals(1, checks)
            running.value = false
            runCurrent()
            assertEquals(2, checks)
            assertIs<InAppNotification.SelfUpdateReady>(assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun repeatedReleaseIsNotQueuedAgainButNewBuildWithSameDisplayVersionIs() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val clock = TestTimeSource()
            var releaseId = "1.6.1.10422"
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) =
                    SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = releaseId)
                override suspend fun resumeUpdate() = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage, clock, 24.hours), {})
            lifecycle.resume(); runCurrent()
            val first = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            assertEquals(releaseId, assertIs<InAppNotification.SelfUpdateReady>(first.notification).releaseId)
            clock += 24.hours
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(0, assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).pending.size)
            releaseId = "1.6.1.10423"
            clock += 24.hours
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            val state = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            assertEquals(releaseId, assertIs<InAppNotification.SelfUpdateReady>(state.pending.single()).releaseId)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun failedScheduledCheckCanRetryOnNextForeground() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val clock = TestTimeSource()
            var checks = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return if (checks == 2) SelfUpdateResult.Error(IllegalStateException("offline")) else SelfUpdateResult.NoUpdates
                }
                override suspend fun resumeUpdate() = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage, clock, 24.hours), {})
            lifecycle.resume(); runCurrent()
            clock += 24.hours
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(2, checks)
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(3, checks)
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(3, checks)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun rechecksOnForegroundOnlyAfterIntervalAndRestoresPendingFirst() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val clock = TestTimeSource()
            var checks = 0
            var pending = false
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return SelfUpdateResult.NoUpdates
                }
                override suspend fun resumeUpdate(): SelfUpdateResult =
                    if (pending) SelfUpdateResult.DownloadProgress(10, false) else SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage, clock, 24.hours), {})
            lifecycle.resume()
            runCurrent()
            assertEquals(1, checks)
            clock += 23.hours
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(1, checks)
            clock += 1.hours
            pending = true
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(1, checks)
            pending = false
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(2, checks)
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(2, checks)
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test
    fun initialOfflineCheckIsRetriedOnNextForeground() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            var checks = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return if (checks == 1) SelfUpdateResult.Error(IllegalStateException("offline"))
                    else SelfUpdateResult.SelfUpdateReady("1.6.1", "notes")
                }
                override suspend fun resumeUpdate() = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = false
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(
                DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {},
            )
            lifecycle.resume()
            runCurrent()
            assertEquals(1, checks)
            lifecycle.pause()
            lifecycle.resume()
            runCurrent()
            assertEquals(2, checks)
            assertIs<InAppNotification.SelfUpdateReady>(assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
            lifecycle.pause()
            lifecycle.resume()
            runCurrent()
            assertEquals(2, checks)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }
    @Test
    fun leavingForegroundCancelsCheckAndRetriesAfterReturning() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val running = MutableStateFlow(false)
            val finishCheck = CompletableDeferred<Unit>()
            var checks = 0
            var cancelled = 0
            var flowsStarted = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    running.value = true
                    try {
                        if (checks == 1) finishCheck.await()
                        flowsStarted++
                        return SelfUpdateResult.Complete
                    } finally {
                        if (checks == 1 && flowsStarted == 0) cancelled++
                        running.value = false
                    }
                }
                override suspend fun resumeUpdate() = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = running
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = false
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            InAppNotificationComponentBase(
                DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {},
            )
            lifecycle.resume()
            runCurrent()
            lifecycle.pause()
            runCurrent()
            assertEquals(1, cancelled)
            finishCheck.complete(Unit)
            runCurrent()
            assertEquals(0, flowsStarted)
            lifecycle.resume()
            runCurrent()
            assertEquals(2, checks)
            assertEquals(1, flowsStarted)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }
    @Test
    fun notificationsQueuedWhilePausedAreDisplayedInOrderAndStaleActionsAreIgnored() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            var installs = 0
            var dismissals = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.NoUpdates
                override suspend fun resumeUpdate() = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val component = InAppNotificationComponentBase(
                DefaultComponentContext(lifecycle), storage,
                InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage), {},
            )
            val started = InAppNotification.Error(titleId = 1, descId = 2, actionTextId = null, action = null)
            val ready = InAppNotification.SelfUpdateReady({ installs++ }, "1.6.1", "", readyToInstall = true, onDismiss = { dismissals++ })
            val error = InAppNotification.SelfUpdateError(IllegalStateException("offline"))
            storage.addNotification(started)
            storage.addNotification(ready)
            storage.addNotification(error)
            runCurrent()
            lifecycle.resume()
            runCurrent()
            assertEquals(started, assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
            component.obtainEvent(InAppNotificationStore.Intent.OpenNotificationScreen(ready))
            assertEquals(0, installs)
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(ready))
            assertEquals(0, dismissals)
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(started))
            assertEquals(ready, assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(started))
            assertEquals(ready, assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
            component.obtainEvent(InAppNotificationStore.Intent.OpenNotificationScreen(ready))
            assertEquals(1, installs)
            assertEquals(0, dismissals)
            assertEquals(error, assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value).notification)
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(error))
            assertEquals(InAppNotificationStore.State.NotPresent, component.state.value)
            component.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(ready))
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(ready))
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(ready))
            assertEquals(1, dismissals)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    @Test
    fun completedDownloadSupersedesObsoleteDownloadStatus() {
        val ready = InAppNotification.SelfUpdateReady({}, "1.6.1", "", readyToInstall = true)
        val reducer = com.markettwits.selfupdater.components.notification.store.InAppNotificationReducer
        with(reducer) {
            val started: InAppNotificationStore.State = InAppNotificationStore.State.NotPresent.reduce(
                InAppNotificationStore.Message.Enqueue(InAppNotification.SelfUpdateStarted)
            )
            val completed = started.reduce(InAppNotificationStore.Message.Enqueue(ready))
            assertEquals(ready, assertIs<InAppNotificationStore.State.ShownNotification>(completed).notification)
            assertEquals(completed, completed.reduce(InAppNotificationStore.Message.Dismiss(InAppNotification.SelfUpdateStarted)))
        }
    }

    @Test
    fun readyToInstallReplacesOnlyTheOfferForTheSameRelease() {
        val reducer = com.markettwits.selfupdater.components.notification.store.InAppNotificationReducer
        val offer = InAppNotification.SelfUpdateReady({}, "1.6.1", "", releaseId = "1.6.1.10422")
        val other = InAppNotification.SelfUpdateReady({}, "1.6.1", "", releaseId = "1.6.1.10423")
        val ready = InAppNotification.SelfUpdateReady({}, "1.6.1", "", readyToInstall = true, releaseId = "1.6.1.10422")
        with(reducer) {
            val initial: InAppNotificationStore.State = InAppNotificationStore.State.NotPresent
            val showingOffer = initial.reduce(InAppNotificationStore.Message.Enqueue(offer))
            val replaced = showingOffer.reduce(InAppNotificationStore.Message.Enqueue(ready))
            assertEquals(ready, assertIs<InAppNotificationStore.State.ShownNotification>(replaced).notification)
            val showingOther = initial.reduce(InAppNotificationStore.Message.Enqueue(other))
            val queued = showingOther.reduce(InAppNotificationStore.Message.Enqueue(ready))
            assertEquals(other, assertIs<InAppNotificationStore.State.ShownNotification>(queued).notification)
            assertEquals(listOf(ready), queued.pending)
        }
    }

    @Test
    fun foregroundChecksOnceThenRestoresWithoutOpeningOnDismiss() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            var checks = 0
            var resumes = 0
            var opens = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return SelfUpdateResult.SelfUpdateReady("1.6.1", "", informationalOnly = true)
                }
                override suspend fun resumeUpdate(): SelfUpdateResult {
                    resumes++
                    return SelfUpdateResult.NoUpdates
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "test"
                override fun isSelfUpdateCanManualCheck() = false
            }
            val storage = InAppNotificationStorageImpl(backgroundScope)
            val factory = InAppNotificationStoreFactory(DefaultStoreFactory(), updater, storage)
            val component = InAppNotificationComponentBase(DefaultComponentContext(lifecycle), storage, factory) { opens++ }
            runCurrent()
            assertEquals(0, checks)
            lifecycle.resume()
            runCurrent()
            assertEquals(1, checks)
            val shown = assertIs<InAppNotificationStore.State.ShownNotification>(component.state.value)
            val notification = assertIs<InAppNotification.SelfUpdateReady>(shown.notification)
            assertEquals(true, notification.informationalOnly)
            component.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(notification))
            runCurrent()
            assertEquals(InAppNotificationStore.State.NotPresent, component.state.value)
            assertEquals(0, opens)
            lifecycle.pause()
            lifecycle.resume()
            runCurrent()
            assertEquals(1, checks)
            assertEquals(1, resumes)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }
}

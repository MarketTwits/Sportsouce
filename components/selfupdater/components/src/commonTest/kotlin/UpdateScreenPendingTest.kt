import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.setMain
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateReducer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateScreenPendingTest {
    @Test
    fun reopeningPreparedApkRestoresItsReleaseNotesWithoutAnotherNetworkCheck() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var checks = 0
            val api = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return SelfUpdateResult.ReadyToInstall
                }
                override fun pendingRelease() = SelfUpdateResult.SelfUpdateReady("1.6.1.10422", "Saved release notes")
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), api).create(null)
            store.init(); runCurrent()
            assertTrue(store.state.readyToInstall)
            assertEquals("1.6.1.10422", store.state.newAppInfo?.version)
            assertEquals("Saved release notes", store.state.newAppInfo?.description)
            assertEquals(1, checks)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun retryAndTerminalStatesClearStaleDownloadDetails() {
        with(SelfUpdateReducer) {
            val initial = SelfUpdateStore.State(newAppInfo = NewAppVersion("1.6.1", "old notes"))
            val paused = initial.reduce(SelfUpdateStore.Message.DownloadPending(false, 42, true))
                .reduce(SelfUpdateStore.Message.TransferProgress(99))
            val checking = paused.reduce(SelfUpdateStore.Message.Loading(true))
            assertFalse(checking.downloadPaused)
            assertFalse(checking.downloadActive)
            assertEquals(null, checking.pendingMessage)
            assertEquals(null, checking.transferPercent)
            for (message in listOf(SelfUpdateStore.Message.NoUpdates, SelfUpdateStore.Message.DownloadFailed("offline"),
                SelfUpdateStore.Message.Status("unsupported"), SelfUpdateStore.Message.NewAppAvailable(NewAppVersion("1.6.2", "notes")))) {
                val terminal = paused.reduce(message)
                assertFalse(terminal.downloadPaused)
                assertEquals(null, terminal.transferPercent)
            }
            assertEquals(null, paused.reduce(SelfUpdateStore.Message.NoUpdates).newAppInfo)
        }
    }

    @Test
    fun busyInstallerWaitsAndRepeatedTapsDoNotStartDuplicateInstallations() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val busy = MutableStateFlow(true)
            var installations = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.ReadyToInstall
                override suspend fun installUpdate(): SelfUpdateResult {
                    if (busy.value) return SelfUpdateResult.InProgress
                    installations++
                    return SelfUpdateResult.ReadyToInstall
                }
                override fun getInProgressState() = busy
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init(); runCurrent()
            store.accept(SelfUpdateStore.Intent.OnClickInstall); runCurrent()
            assertTrue(store.state.installerOpening)
            store.accept(SelfUpdateStore.Intent.OnClickInstall); runCurrent()
            assertEquals(0, installations)
            busy.value = false; runCurrent()
            assertEquals(1, installations)
            assertFalse(store.state.installerOpening)
            assertTrue(store.state.readyToInstall)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun readyUpdateCanBeInstalledDirectlyFromScreen() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var installations = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.ReadyToInstall
                override suspend fun installUpdate(): SelfUpdateResult {
                    installations++
                    return SelfUpdateResult.ReadyToInstall
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            runCurrent()
            assertTrue(store.state.readyToInstall)
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.OnClickInstall)
            runCurrent()
            assertEquals(1, installations)
            assertTrue(store.state.readyToInstall)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun startingUpdateReplacesPreviousFailureAfterDialogIsDismissed() {
        val reducer = com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateReducer
        val initial = com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State(newAppInfo = null)
        with(reducer) {
            val failed = initial.reduce(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message.DownloadFailed("error"))
            val started = failed.reduce(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message.DownloadStarted)
            val dismissed = started.reduce(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message.ConsumedEvent)
            assertFalse(dismissed.isFailed)
            assertTrue(dismissed.pendingMessage.orEmpty().contains("запущено"))
        }
    }
    @Test
    fun returningFromInstallerRechecksReadyState() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var installed = false
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.ReadyToInstall
                override suspend fun resumeUpdate() = if (installed) SelfUpdateResult.NoUpdates else SelfUpdateResult.ReadyToInstall
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenPaused)
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            assertTrue(store.state.pendingMessage.orEmpty().contains("готово"))
            assertFalse(store.state.isSuccess)
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenPaused)
            installed = true
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            assertEquals(null, store.state.pendingMessage)
            assertFalse(store.state.updatesAvailable)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun unsupportedApkIsExplainedAfterDownload() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.DownloadInProgress
                override suspend fun resumeUpdate() = SelfUpdateResult.Unsupported
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            advanceTimeBy(2_001)
            runCurrent()
            assertFalse(store.state.downloadActive)
            assertTrue(store.state.pendingMessage.orEmpty().contains("устройства"))
            assertFalse(store.state.isFailed)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun endedNativeSessionWithStillAvailableUpdateShowsOfferAgain() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.InteractiveUpdateInProgress
                override suspend fun resumeUpdate() = SelfUpdateResult.SelfUpdateReady("1.6.1", "notes")
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            assertTrue(store.state.pendingMessage.orEmpty().contains("окно обновления"))
            assertFalse(store.state.downloadActive)
            advanceTimeBy(2_001)
            runCurrent()
            assertTrue(store.state.updatesAvailable)
            assertFalse(store.state.downloadActive)
            assertEquals("1.6.1", store.state.newAppInfo?.version)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun downloadAlreadyInProgressWithoutPercentIsReconciledWhenReady() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var reads = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.DownloadInProgress
                override suspend fun resumeUpdate(): SelfUpdateResult {
                    reads++
                    return if (reads == 1) SelfUpdateResult.DownloadProgress(50, false) else SelfUpdateResult.ReadyToInstall
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            assertTrue(store.state.downloadActive)
            advanceTimeBy(2_001)
            runCurrent()
            assertEquals(50, store.state.downloadPercent)
            advanceTimeBy(2_001)
            runCurrent()
            assertFalse(store.state.downloadActive)
            assertTrue(store.state.pendingMessage.orEmpty().contains("готово"))
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun openingWithReleaseInfoRestoresAnExistingDownload() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var checks = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return SelfUpdateResult.NoUpdates
                }
                override suspend fun resumeUpdate() = SelfUpdateResult.DownloadProgress(50, false)
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(NewAppVersion("1.6.1", "notes"))
            store.init()
            runCurrent()
            assertEquals(0, checks)
            assertTrue(store.state.downloadActive)
            assertEquals(50, store.state.downloadPercent)
            assertTrue(store.state.pendingMessage.orEmpty().contains("50%"))
            assertFalse(store.state.isLoading)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun openingWithStaleReleaseInfoChecksTheCurrentVersion() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var checks = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    checks++
                    return SelfUpdateResult.NoUpdates
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(NewAppVersion("1.6.1", "notes"))
            store.init()
            runCurrent()
            assertEquals(1, checks)
            assertFalse(store.state.updatesAvailable)
            assertFalse(store.state.isLoading)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun openingWithReleaseInfoShowsTheLatestAvailableVersion() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.SelfUpdateReady("1.6.2", "new notes")
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(NewAppVersion("1.6.1", "old notes"))
            store.init()
            runCurrent()
            assertTrue(store.state.updatesAvailable)
            assertEquals("1.6.2", store.state.newAppInfo?.version)
            assertEquals("new notes", store.state.newAppInfo?.description)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun desktopCompletionAfterReopeningShowsInstallerInstructions() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var reads = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.DownloadProgress(50, false)
                override suspend fun resumeUpdate(): SelfUpdateResult {
                    reads++
                    return SelfUpdateResult.DesktopInstallerDownloaded
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            advanceTimeBy(2_001)
            runCurrent()
            assertTrue(store.state.readyToInstall)
            assertTrue(store.state.pendingMessage.orEmpty().contains("готово"))
            assertFalse(store.state.downloadActive)
            assertFalse(store.state.isFailed)
            advanceTimeBy(5_000)
            runCurrent()
            assertEquals(1, reads)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test
    fun foregroundProgressUpdatesAndStopsWhenScreenIsPaused() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            var reads = 0
            var ready = false
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.DownloadProgress(10, false)
                override suspend fun resumeUpdate(): SelfUpdateResult {
                    reads++
                    return if (ready) SelfUpdateResult.ReadyToInstall else SelfUpdateResult.DownloadProgress(50, false)
                }
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            runCurrent()
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ConsumedEvent)
            advanceTimeBy(2_001)
            runCurrent()
            assertEquals(1, reads)
            assertTrue(store.state.pendingMessage.orEmpty().contains("50%"))
            assertEquals(50, store.state.downloadPercent)
            assertTrue(store.state.downloadActive)
            assertFalse(store.state.isSuccess)
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenPaused)
            advanceTimeBy(5_000)
            runCurrent()
            assertEquals(1, reads)
            ready = true
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ScreenResumed)
            advanceTimeBy(2_001)
            runCurrent()
            assertEquals(2, reads)
            assertTrue(store.state.pendingMessage.orEmpty().contains("готово"))
            assertFalse(store.state.downloadActive)
            assertEquals(null, store.state.downloadPercent)
            advanceTimeBy(5_000)
            runCurrent()
            assertEquals(2, reads)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun progressAndPausedStateAreVisibleWithoutClaimingCompletion() {
        val reducer = com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateReducer
        val state = com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State(newAppInfo = null)
        with(reducer) {
            val running = state.reduce(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message.DownloadPending(false, 50, false))
            assertTrue(running.pendingMessage.orEmpty().contains("50%"))
            val paused = running.reduce(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message.DownloadPending(false, 99, true))
            assertTrue(paused.pendingMessage.orEmpty().contains("приостановлена"))
            assertFalse(paused.pendingMessage.orEmpty().contains("готово"))
        }
    }
    @Test
    fun busyUpdaterDoesNotShowNoUpdatesAndRetriesAfterOtherOperation() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val running = MutableStateFlow(true)
            var attempts = 0
            val updater = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
                    attempts++
                    return if (running.value) SelfUpdateResult.InProgress else SelfUpdateResult.SelfUpdateReady("1.6.1", "notes")
                }
                override fun getInProgressState() = running
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
            store.init()
            runCurrent()
            assertTrue(store.state.isLoading)
            assertFalse(store.state.updatesAvailable)
            assertEquals(1, attempts)
            store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.OnClickCheck)
            runCurrent()
            assertEquals(1, attempts)
            running.value = false
            runCurrent()
            assertEquals(2, attempts)
            assertTrue(store.state.updatesAvailable)
            assertFalse(store.state.isLoading)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }
    @Test
    fun ongoingOrReadyDownloadDoesNotLeaveScreenLoadingOrClaimNoUpdates() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            for (result in listOf(SelfUpdateResult.DownloadInProgress, SelfUpdateResult.ReadyToInstall)) {
                val updater = object : SelfUpdaterApi {
                    override suspend fun startCheckUpdate(manual: Boolean) = result
                    override fun getInProgressState() = MutableStateFlow(false)
                    override fun getInstallSourceName() = "GitHub"
                    override fun isSelfUpdateCanManualCheck() = true
                }
                val store = SelfUpdateStoreFactory(DefaultStoreFactory(), updater).create(null)
                store.init()
                runCurrent()
                assertFalse(store.state.isLoading)
                assertTrue(store.state.isSuccess)
                assertTrue(store.state.message.contains(if (result == SelfUpdateResult.ReadyToInstall) "готово" else "загружается"))
                store.accept(com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent.ConsumedEvent)
                runCurrent()
                assertTrue(store.state.pendingMessage.orEmpty().isNotBlank())
                store.dispose()
            }
        } finally { Dispatchers.resetMain() }
    }
}

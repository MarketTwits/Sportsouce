package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SparkleUpdaterSourceTest {
    private class FakeGateway : SparkleGateway {
        var discovery: SparkleDiscovery = SparkleDiscovery.Available("1.6.1", 10422, "notes")
        var starts = 0
        var discoveries = 0
        var accepts = true
        var active = false
        var failure: Exception? = null
        override suspend fun discover(): SparkleDiscovery { discoveries++; failure?.let { throw it }; return discovery }
        override suspend fun startInteractiveUpdate(): Boolean { starts++; return accepts }
        override suspend fun hasActiveSession() = active
    }

    @Test
    fun discoveryIsReadOnlyAndOnlyExplicitActionStartsUpdate() = runTest {
        val gateway = FakeGateway()
        val source = SparkleUpdaterSource(gateway) { 10421 }
        assertEquals(SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = "1.6.1.10422"), source.checkUpdate(false))
        assertEquals(0, gateway.starts)
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(true))
        assertEquals(1, gateway.starts)
        gateway.accepts = false
        assertIs<SelfUpdateResult.Error>(source.checkUpdate(true))
    }

    @Test
    fun distinguishesUnavailableInvalidAndFailedChecks() = runTest {
        val gateway = FakeGateway()
        val source = SparkleUpdaterSource(gateway) { 10421 }
        gateway.discovery = SparkleDiscovery.Current
        assertEquals(SelfUpdateResult.NoUpdates, source.checkUpdate(false))
        gateway.discovery = SparkleDiscovery.Unsupported
        assertEquals(SelfUpdateResult.Unsupported, source.checkUpdate(false))
        gateway.discovery = SparkleDiscovery.Available("1.6.1", 10421, "")
        assertIs<SelfUpdateResult.Error>(source.checkUpdate(true))
        assertEquals(0, gateway.starts)
        gateway.discovery = SparkleDiscovery.Available("01.6.1", 10422, "")
        assertIs<SelfUpdateResult.Error>(source.checkUpdate(true))
        gateway.failure = IllegalStateException("network")
        assertIs<SelfUpdateResult.Error>(source.checkUpdate(false))
    }

    @Test
    fun restoresAnActiveNativeSessionAndNeverStartsItOnResume() = runTest {
        val gateway = FakeGateway()
        val source = SparkleUpdaterSource(gateway) { 10421 }
        gateway.active = true
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.resumeUpdate())
        gateway.active = false
        assertIs<SelfUpdateResult.SelfUpdateReady>(source.resumeUpdate())
        assertEquals(SelfUpdateResult.NoUpdates, source.resumeUpdate())
        assertEquals(0, gateway.starts)
    }

    @Test
    fun closingBeforeFirstPollRechecksAndRetriesFailedReconciliation() = runTest {
        val gateway = FakeGateway()
        val source = SparkleUpdaterSource(gateway) { 10421 }
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(true))
        gateway.failure = IllegalStateException("network")
        assertIs<SelfUpdateResult.Error>(source.resumeUpdate())
        gateway.failure = null
        assertEquals(SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = "1.6.1.10422"), source.resumeUpdate())
        assertEquals(1, gateway.starts)
    }

    @Test
    fun repeatedCheckDuringInteractiveSessionDoesNotStartAnotherDiscovery() = runTest {
        val gateway = FakeGateway()
        val source = SparkleUpdaterSource(gateway) { 10421 }
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(true))
        gateway.active = true
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(false))
        assertEquals(1, gateway.discoveries)
        assertEquals(1, gateway.starts)
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(true))
        assertEquals(1, gateway.discoveries)
        assertEquals(2, gateway.starts)
        gateway.accepts = false
        assertEquals(SelfUpdateResult.InteractiveUpdateInProgress, source.checkUpdate(true))
        assertEquals(1, gateway.discoveries)
        gateway.active = false
        assertIs<SelfUpdateResult.SelfUpdateReady>(source.checkUpdate(false))
        assertEquals(2, gateway.discoveries)
    }

    @Test
    fun cancelledStartIsReconciledEvenWhenNativeCommandFinishesLater() = runTest {
        val started = CompletableDeferred<Unit>()
        val gateway = object : SparkleGateway {
            var active = false
            override suspend fun discover() = SparkleDiscovery.Available("1.6.1", 10422, "notes")
            override suspend fun startInteractiveUpdate(): Boolean {
                started.complete(Unit)
                CompletableDeferred<Unit>().await()
                return true
            }
            override suspend fun hasActiveSession() = active
        }
        val source = SparkleUpdaterSource(gateway) { 10421 }
        val request = async { source.checkUpdate(true) }
        started.await()
        request.cancel()
        request.join()
        // The native side may have both opened and closed before the first poll.
        assertIs<SelfUpdateResult.SelfUpdateReady>(source.resumeUpdate())
    }

    @Test
    fun cancellationReleasesLockAndParallelRequestsDoNotStartASecondFlow() = runTest {
        val started = CompletableDeferred<Unit>()
        val gate = CompletableDeferred<Unit>()
        val gateway = object : SparkleGateway {
            override suspend fun discover(): SparkleDiscovery { started.complete(Unit); gate.await(); return SparkleDiscovery.Current }
            override suspend fun startInteractiveUpdate() = error("unexpected")
            override suspend fun hasActiveSession() = false
        }
        val source = SparkleUpdaterSource(gateway) { 10421 }
        val first = async { source.checkUpdate(false) }
        started.await()
        assertEquals(SelfUpdateResult.InProgress, source.checkUpdate(true))
        first.cancel()
        first.join()
        gate.complete(Unit)
        assertEquals(SelfUpdateResult.NoUpdates, source.checkUpdate(false))
    }
}

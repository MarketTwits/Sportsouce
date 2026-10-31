package com.markettwits.selfupdater.googleplay.api

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GooglePlayUpdateControllerTest {
    private class FakeGateway : PlayUpdateGateway<Int> {
        var info = PlayUpdateSnapshot(1, PlayAvailability.Available, immediateAllowed = true, downloaded = false)
        var reads = 0
        var failRead = false
        var startResult = true
        val started = mutableListOf<Int>()

        override suspend fun getUpdateInfo(): PlayUpdateSnapshot<Int> {
            reads++
            if (failRead) error("Play unavailable")
            return info
        }

        override fun startImmediateUpdate(handle: Int): Boolean {
            started += handle
            return startResult
        }
    }

    @Test
    fun usesFreshInfoAndStartsOnlyAllowedImmediateFlow() = runTest {
        val gateway = FakeGateway()
        val controller = GooglePlayUpdateController(gateway)
        assertEquals(PlayCheckResult.Started, controller.check())
        gateway.info = gateway.info.copy(handle = 2, immediateAllowed = false)
        assertEquals(PlayCheckResult.Unavailable, controller.check())
        assertEquals(2, gateway.reads)
        assertEquals(listOf(1), gateway.started)
    }

    @Test
    fun resumesImmediateFlowAndRecoversAlreadyDownloadedUpdate() = runTest {
        val gateway = FakeGateway()
        val controller = GooglePlayUpdateController(gateway)
        gateway.info = gateway.info.copy(availability = PlayAvailability.InProgress, immediateAllowed = false)
        assertEquals(PlayCheckResult.Started, controller.resume())
        gateway.info = gateway.info.copy(downloaded = true)
        assertEquals(PlayCheckResult.ReadyToComplete, controller.resume())
        assertEquals(listOf(1), gateway.started)
    }

    @Test
    fun foregroundingDoesNotStartANewAvailableUpdate() = runTest {
        val gateway = FakeGateway()
        assertEquals(PlayCheckResult.NoUpdate, GooglePlayUpdateController(gateway).resume())
        assertEquals(emptyList(), gateway.started)
    }

    @Test
    fun handlesUnavailableResultsAndRetriesAfterFailure() = runTest {
        val gateway = FakeGateway()
        val controller = GooglePlayUpdateController(gateway)
        gateway.failRead = true
        assertFailsWith<IllegalStateException> { controller.check() }
        gateway.failRead = false
        gateway.info = gateway.info.copy(availability = PlayAvailability.NotAvailable)
        assertEquals(PlayCheckResult.NoUpdate, controller.check())
        gateway.info = gateway.info.copy(availability = PlayAvailability.Unknown)
        assertEquals(PlayCheckResult.Unavailable, controller.check())
        gateway.info = gateway.info.copy(availability = PlayAvailability.Available)
        gateway.startResult = false
        assertEquals(PlayCheckResult.Unavailable, controller.check())
    }
}

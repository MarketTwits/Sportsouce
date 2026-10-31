package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.nio.file.Path

@OptIn(ExperimentalCoroutinesApi::class)
class SparkleAsyncGatewayTest {
    @Test
    fun nativeDriverRejectsAnUnconfiguredHostWhenOptedIn() = runBlocking {
        val path = System.getenv("SPORTSAUCE_TEST_SPARKLE_JNI") ?: return@runBlocking
        java.awt.Toolkit.getDefaultToolkit()
        val gateway = SparkleAsyncGateway(SparkleJniDriver.load(Path.of(path)), timeoutMillis = 5_000)
        val failure = assertFailsWith<IllegalStateException> { gateway.discover() }
        assertEquals("Sparkle could not check for updates", failure.message)
        println("JNI rejected unconfigured Sparkle host")
    }

    private class Driver : SparkleAsyncDriver {
        lateinit var listener: (Long, SparkleReply) -> Unit
        val requests = mutableListOf<Pair<Long, SparkleCommand>>()
        override fun setReplyListener(listener: (Long, SparkleReply) -> Unit) { this.listener = listener }
        override fun request(id: Long, command: SparkleCommand) { requests += id to command }
    }

    @Test
    fun routesOutOfOrderRepliesToTheirOwnRequests() = runTest {
        val driver = Driver()
        val gateway = SparkleAsyncGateway(driver)
        val discovery = async { gateway.discover() }
        val session = async { gateway.hasActiveSession() }
        runCurrent()
        driver.listener(driver.requests[1].first, SparkleReply.Flag(true))
        driver.listener(driver.requests[0].first, SparkleReply.Discovery(SparkleDiscovery.Current))
        assertEquals(true, session.await())
        assertEquals(SparkleDiscovery.Current, discovery.await())
    }

    @Test
    fun timeoutAndCancellationDiscardLateReplies() = runTest {
        val driver = Driver()
        val gateway = SparkleAsyncGateway(driver, timeoutMillis = 1000)
        val cancelled = async { gateway.hasActiveSession() }
        runCurrent()
        val cancelledId = driver.requests.last().first
        cancelled.cancel()
        cancelled.join()
        driver.listener(cancelledId, SparkleReply.Flag(true))
        val timed = async { runCatching { gateway.discover() } }
        runCurrent()
        val timedId = driver.requests.last().first
        advanceTimeBy(1001)
        runCurrent()
        assertFailsWith<IllegalStateException> { timed.await().getOrThrow() }
        driver.listener(timedId, SparkleReply.Discovery(SparkleDiscovery.Current))
        val next = async { gateway.startInteractiveUpdate() }
        runCurrent()
        driver.listener(driver.requests.last().first, SparkleReply.Flag(false))
        assertEquals(false, next.await())
    }

    @Test
    fun invalidOrFailedNativeReplyDoesNotBecomeNoUpdates() = runTest {
        val driver = Driver()
        val gateway = SparkleAsyncGateway(driver)
        val result = async { runCatching { gateway.discover() } }
        runCurrent()
        driver.listener(driver.requests.last().first, SparkleReply.Flag(false))
        assertFailsWith<IllegalStateException> { result.await().getOrThrow() }
    }

    @Test
    fun synchronousDriverFailureRemainsAnError() = runTest {
        val driver = object : SparkleAsyncDriver {
            override fun setReplyListener(listener: (Long, SparkleReply) -> Unit) = Unit
            override fun request(id: Long, command: SparkleCommand) { error("bridge unavailable") }
        }
        val gateway = SparkleAsyncGateway(driver)
        assertFailsWith<IllegalStateException> { gateway.discover() }
    }
}

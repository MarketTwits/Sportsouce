package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Thread-safe messages exchanged with a platform driver; no native types escape the JVM layer. */
internal enum class SparkleCommand { Discover, StartInteractive, SessionActive }
internal sealed interface SparkleReply {
    data class Discovery(val value: SparkleDiscovery) : SparkleReply
    data class Flag(val value: Boolean) : SparkleReply
    data class Failure(val message: String) : SparkleReply
}

/** Implementations must deliver replies by id and perform Sparkle calls on the AppKit main thread. */
internal interface SparkleAsyncDriver {
    fun setReplyListener(listener: (Long, SparkleReply) -> Unit)
    fun request(id: Long, command: SparkleCommand)
}

/** Bridges callbacks from the platform driver to the updater's suspend API. */
internal class SparkleAsyncGateway(
    private val driver: SparkleAsyncDriver,
    private val timeoutMillis: Long = 30_000,
) : SparkleGateway {
    private val nextId = AtomicLong(0)
    private val pending = ConcurrentHashMap<Long, CancellableContinuation<SparkleReply>>()

    init {
        require(timeoutMillis > 0)
        driver.setReplyListener { id, reply -> pending.remove(id)?.resume(reply) }
    }

    override suspend fun discover(): SparkleDiscovery = when (val reply = request(SparkleCommand.Discover)) {
        is SparkleReply.Discovery -> reply.value
        is SparkleReply.Failure -> error(reply.message)
        else -> error("Unexpected native response to update discovery")
    }

    override suspend fun startInteractiveUpdate(): Boolean = flag(SparkleCommand.StartInteractive)
    override suspend fun hasActiveSession(): Boolean = flag(SparkleCommand.SessionActive)

    private suspend fun flag(command: SparkleCommand): Boolean = when (val reply = request(command)) {
        is SparkleReply.Flag -> reply.value
        is SparkleReply.Failure -> error(reply.message)
        else -> error("Unexpected native response to $command")
    }

    private suspend fun request(command: SparkleCommand): SparkleReply = withTimeoutOrNull(timeoutMillis) {
        suspendCancellableCoroutine { continuation ->
            val id = nextId.incrementAndGet()
            pending[id] = continuation
            continuation.invokeOnCancellation { pending.remove(id, continuation) }
            try {
                driver.request(id, command)
            } catch (failure: Exception) {
                if (pending.remove(id, continuation)) continuation.resumeWithException(failure)
            }
        }
    } ?: throw IllegalStateException("Native update request timed out")
}

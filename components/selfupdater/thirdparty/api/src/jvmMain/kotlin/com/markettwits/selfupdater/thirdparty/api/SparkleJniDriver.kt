package com.markettwits.selfupdater.thirdparty.api

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean

/** One app-scoped driver; the native side owns its controller for the process lifetime. */
internal class SparkleJniDriver private constructor() : SparkleAsyncDriver {
    @Volatile private var replyListener: ((Long, SparkleReply) -> Unit)? = null

    override fun setReplyListener(listener: (Long, SparkleReply) -> Unit) {
        check(replyListener == null) { "Sparkle driver is already bound" }
        replyListener = listener
    }

    override fun request(id: Long, command: SparkleCommand) {
        check(replyListener != null) { "Sparkle driver has no listener" }
        nativeRequest(id, when (command) {
            SparkleCommand.Discover -> 1
            SparkleCommand.StartInteractive -> 2
            SparkleCommand.SessionActive -> 3
        })
    }

    // Called from the AppKit main thread by JNI. Invalid replies fail closed.
    @Suppress("unused")
    private fun onNativeReply(id: Long, code: Int, version: String?, build: String?, description: String?) {
        val reply = when (code) {
            0 -> SparkleReply.Discovery(SparkleDiscovery.Current)
            1 -> {
                val buildNumber = build?.toLongOrNull()
                if (version.isNullOrBlank() || buildNumber == null || buildNumber <= 0) SparkleReply.Failure("Invalid Sparkle release metadata")
                else SparkleReply.Discovery(SparkleDiscovery.Available(version, buildNumber, description.orEmpty()))
            }
            2 -> SparkleReply.Discovery(SparkleDiscovery.Unsupported)
            3 -> SparkleReply.Failure("Sparkle could not check for updates")
            4 -> SparkleReply.Flag(true)
            5 -> SparkleReply.Flag(false)
            else -> SparkleReply.Failure("Invalid response from Sparkle")
        }
        replyListener?.invoke(id, reply)
    }

    private external fun nativeBind()
    private external fun nativeRequest(id: Long, command: Int)

    companion object {
        private val created = AtomicBoolean(false)

        /** Caller is responsible for verifying the packaged native library and publisher first. */
        fun load(bridge: Path): SparkleJniDriver {
            check(created.compareAndSet(false, true)) { "Sparkle driver already created" }
            try {
                require(Files.isRegularFile(bridge)) { "Sparkle bridge is missing" }
                System.load(bridge.toRealPath().toString())
                return SparkleJniDriver().also { it.nativeBind() }
            } catch (failure: Throwable) {
                created.set(false)
                throw failure
            }
        }
    }
}

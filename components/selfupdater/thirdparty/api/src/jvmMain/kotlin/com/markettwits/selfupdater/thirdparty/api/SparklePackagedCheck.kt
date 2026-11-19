package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import java.nio.file.Path

/** Offline check for code retained in a packaged runtime; never loads native code. */
object SparklePackagedCheck {
    /** Only invoked by the explicit packaged smoke command with a test library path. */
    suspend fun verifyNative(library: String, expectedCode: Int) {
        val source = SparkleUpdaterSource(SparkleAsyncGateway(SparkleJniDriver.load(Path.of(library)))) { 10422 }
        val result = source.checkUpdate(false)
        val code = when (result) {
            is SelfUpdateResult.SelfUpdateReady -> {
                check(result.versionName == "99.0.0" && result.releaseId == "99.0.0.999999")
                1
            }
            SelfUpdateResult.NoUpdates -> 0
            SelfUpdateResult.Unsupported -> 2
            is SelfUpdateResult.Error -> 3
            else -> error("Unexpected native smoke result")
        }
        check(code == expectedCode) { "Unexpected native smoke result: $code" }
        println("SELF_UPDATER_NATIVE_TEST_OK=$code")
    }

    suspend fun verify() {
        val driver = object : SparkleAsyncDriver {
            lateinit var listener: (Long, SparkleReply) -> Unit
            override fun setReplyListener(listener: (Long, SparkleReply) -> Unit) { this.listener = listener }
            override fun request(id: Long, command: SparkleCommand) {
                val reply = when (command) {
                    SparkleCommand.Discover -> SparkleReply.Discovery(SparkleDiscovery.Available("1.6.1", 10422, "notes"))
                    SparkleCommand.StartInteractive -> SparkleReply.Flag(true)
                    SparkleCommand.SessionActive -> SparkleReply.Flag(false)
                }
                listener(id, reply)
            }
        }
        val source = SparkleUpdaterSource(SparkleAsyncGateway(driver)) { 10421 }
        check(source.checkUpdate(false) == SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = "1.6.1.10422"))
        check(source.checkUpdate(true) == SelfUpdateResult.InteractiveUpdateInProgress)
        check(source.resumeUpdate() == SelfUpdateResult.SelfUpdateReady("1.6.1", "notes", releaseId = "1.6.1.10422"))
    }
}

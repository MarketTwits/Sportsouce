package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

/** Launched only in a disposable packaged-app integration experiment. */
object SparkleJniIntegrationProbe {
    @JvmStatic
    fun main(args: Array<String>) {
      runBlocking {
        java.awt.Toolkit.getDefaultToolkit()
        val driver = SparkleJniDriver.load(Path.of(System.getProperty("sportsauce.probe.library")))
        val source = SparkleUpdaterSource(SparkleAsyncGateway(driver, timeoutMillis = 25_000)) { 10422 }
        val result = source.checkUpdate(false)
        val code = when (result) {
            is SelfUpdateResult.SelfUpdateReady -> {
                check(result.versionName == "99.0.0" && result.releaseId == "99.0.0.999999")
                1
            }
            SelfUpdateResult.NoUpdates -> 0
            SelfUpdateResult.Unsupported -> 2
            is SelfUpdateResult.Error -> 3
            else -> error("Unexpected updater result: $result")
        }
        println("Kotlin updater result: $code")
        check(args.single().toInt() == code)
      }
      kotlin.system.exitProcess(0)
    }
}

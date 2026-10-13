package com.markettwits.core.log

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import com.markettwits.buildkonfig.isDebugMode

fun configureLogging() {
    Logger.setLogWriters(platformLogWriter())
    Logger.setMinSeverity(if (isDebugMode) Severity.Verbose else Severity.Assert)
    TaggedLogger("Sportsauce").infoLog { "Debug logging enabled" }
}

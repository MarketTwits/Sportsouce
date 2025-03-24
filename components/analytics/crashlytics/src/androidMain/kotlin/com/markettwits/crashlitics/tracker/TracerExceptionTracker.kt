package com.markettwits.crashlitics.tracker

import com.markettwits.analitics.crashlytics.BuildConfig
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import ru.ok.tracer.Tracer
import ru.ok.tracer.crash.report.TracerCrashReport

internal class TracerExceptionTracker : ExceptionTracker {

    override fun reportException(exception: Throwable, key: String?) {
        TracerCrashReport.report(exception, key)
    }

    override fun setLog(message: String) {
        TracerCrashReport.log(message)
    }

    override fun setUserId(userId: String) {
        Tracer.setUserId(userId)
    }

    override fun setKey(key: Pair<String, String>) {
        Tracer.setKey(key.first, key.second)
    }

}
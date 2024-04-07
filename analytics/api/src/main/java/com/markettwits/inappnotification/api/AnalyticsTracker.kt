package com.markettwits.inappnotification.api

interface AnalyticsTracker {
    fun logEvent(event: ExceptionEvent)
}

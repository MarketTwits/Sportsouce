package com.markettwits.inappnotification.api.tracker

interface AnalyticsTracker {
    fun reportException(exception: Throwable, key: String? = null)
    fun setLog(message: String)
    fun setUserId(userId: String)
    fun setKey(key: Pair<String, String>)
}

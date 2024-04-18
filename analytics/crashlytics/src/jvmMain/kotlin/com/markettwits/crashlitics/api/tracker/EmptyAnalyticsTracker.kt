package com.markettwits.crashlitics.api.tracker

class EmptyAnalyticsTracker : AnalyticsTracker {
    override fun reportException(exception: Throwable, key: String?) = Unit

    override fun setLog(message: String) = Unit

    override fun setUserId(userId: String) = Unit

    override fun setKey(key: Pair<String, String>) = Unit
}
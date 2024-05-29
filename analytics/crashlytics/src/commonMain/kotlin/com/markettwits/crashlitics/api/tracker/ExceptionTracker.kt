package com.markettwits.crashlitics.api.tracker

interface ExceptionTracker {
    fun reportException(exception: Throwable, key: String? = null)
    fun setLog(message: String)
    fun setUserId(userId: String)
    fun setKey(key: Pair<String, String>)
}

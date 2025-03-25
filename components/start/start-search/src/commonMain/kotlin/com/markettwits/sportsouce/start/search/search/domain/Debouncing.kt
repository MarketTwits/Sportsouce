package com.markettwits.sportsouce.start.search.search.domain

import kotlinx.coroutines.CoroutineScope

interface Debouncing {
    fun debounce(key: String, delay: Long, block: suspend CoroutineScope.() -> Unit)

    companion object {
        const val THREE_SECONDS_DELAY = 3000L
        const val TWO_SECONDS_DELAY = 2000L
        const val ONE_SECONDS_DELAY = 1000L
        const val EMPTY_DELAY = 0L
    }
}
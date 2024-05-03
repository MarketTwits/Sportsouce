package com.markettwits.start_search.search.domain

import kotlinx.coroutines.CoroutineScope

interface Debouncing {
    fun debounce(delay: Long = ONE_SECONDS_DELAY, block: suspend CoroutineScope.() -> Unit)

    companion object {
        const val THREE_SECONDS_DELAY = 3000L
        const val TWO_SECONDS_DELAY = 2000L
        const val ONE_SECONDS_DELAY = 1000L
    }
}
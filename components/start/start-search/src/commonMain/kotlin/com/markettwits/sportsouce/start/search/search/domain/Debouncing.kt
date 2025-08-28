package com.markettwits.sportsouce.start.search.search.domain

import kotlinx.coroutines.CoroutineScope

interface Debouncing {
    fun debounce(key: String, delay: Long, block: suspend CoroutineScope.() -> Unit)
}
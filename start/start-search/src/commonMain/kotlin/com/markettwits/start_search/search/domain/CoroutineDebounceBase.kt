package com.markettwits.start_search.search.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CoroutineDebounceBase(private val scope: CoroutineScope) : Debouncing,
    ReadOnlyProperty<Any?, Debouncing> {

    private var searchJob: Job? = null

    override fun debounce(delay: Long, block: suspend CoroutineScope.() -> Unit) {
        searchJob?.cancel()
        searchJob = scope.launch {
            delay(delay)
            block()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Debouncing = this
}
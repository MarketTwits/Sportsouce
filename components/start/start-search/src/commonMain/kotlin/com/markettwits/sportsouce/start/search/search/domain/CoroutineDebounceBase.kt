package com.markettwits.sportsouce.start.search.search.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CoroutineDebounceBase(private val scope: CoroutineScope) : Debouncing,
    ReadOnlyProperty<Any?, Debouncing> {

    private val debounceJobs = mutableMapOf<String, kotlinx.coroutines.Job>()

    override fun debounce(key: String, delay: Long, block: suspend CoroutineScope.() -> Unit) {
        debounceJobs[key]?.cancel()

        debounceJobs[key] = scope.launch {
            kotlinx.coroutines.delay(delay)
            block()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Debouncing = this
}

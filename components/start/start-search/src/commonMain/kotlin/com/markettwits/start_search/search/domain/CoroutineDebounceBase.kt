package com.markettwits.start_search.search.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@OptIn(FlowPreview::class)
class CoroutineDebounceBase(private val scope: CoroutineScope) : Debouncing,
    ReadOnlyProperty<Any?, Debouncing> {


    private val debounceFlow = MutableSharedFlow<Pair<String, suspend CoroutineScope.() -> Unit>>()

    init {
        scope.launch {
            debounceFlow
                .debounce(300)  // Minimal debounce time to batch frequent changes
                .collect { (key, block) ->
                    scope.launch {
                        block()
                    }
                }
        }
    }

    override fun debounce(key: String, delay: Long, block: suspend CoroutineScope.() -> Unit) {
        scope.launch {
            delay(delay)
            debounceFlow.emit(key to block)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Debouncing = this
}

package com.markettwits.core_ui.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.collectAsScope(scope: CoroutineScope, crossinline block: (T) -> Unit) {
    scope.launch {
        this@collectAsScope.collect {
            block(it)
        }
    }
}
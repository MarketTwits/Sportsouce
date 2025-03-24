package com.markettwits.core_ui.items.extensions

import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes the code inside the block wrapped in a try catch and executes
 * it again in case of an error async. A repeat interval can be set which
 * is missing by default
 */
suspend inline fun <reified T> retryRunCatchingAsync(
    times: Int = 1,
    interval: Long = 0L,
    block: () -> T
): Result<T> {
    var count = 0
    while (true) {
        try {
            return Result.success(block())
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            if (++count >= times) {
                return Result.failure(e)
            } else {
                delay(interval)
            }
        }
    }
}

/**
 * Executes the code inside the block wrapped in a try catch and executes
 * it again in case of an error.
 */
inline fun <reified T> retryRunCatching(
    times: Int = 1,
    block: () -> T
): Result<T> {
    var count = 0
    while (true) {
        try {
            return Result.success(block())
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            if (++count >= times) {
                return Result.failure(e)
            }
        }
    }
}
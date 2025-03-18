package com.markettwits.core_ui.items.extensions

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


data class Fourth<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

data class Fifth<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

suspend inline fun <reified T1, reified T2, reified T3, reified T4> fetchAndMapResults(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Fourth<T1, T2, T3, T4>
): Fourth<T1, T2, T3, T4> {
    val deferredResult = CompletableDeferred<Fourth<T1, T2, T3, T4>>()
    val job = CoroutineScope(coroutineContext).launch {
        val resultDeferred = async { block() }
        val result = resultDeferred.await()
        deferredResult.complete(result)
    }
    val result = deferredResult.await()
    job.join() // Wait for the job to complete

    return result
}

suspend inline fun <reified T1, reified T2, reified T3, reified T4> fetchFourth(
    vararg methods: suspend () -> Any,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
): Fourth<T1, T2, T3, T4> {
    val results = methods.map { method ->
        CoroutineScope(coroutineContext).async {
            method()
        }
    }.awaitAll()
    return Fourth(
        results[0] as T1,
        results[1] as T2,
        results[2] as T3,
        results[3] as T4
    )
}

suspend inline fun <reified T1, reified T2, reified T3, reified T4, reified T5> fetchFifth(
    vararg methods: suspend () -> Any,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
): Fifth<T1, T2, T3, T4, T5> {
    val results = methods.map { method ->
        CoroutineScope(coroutineContext).async {
            method()
        }
    }.awaitAll()
    return Fifth(
        results[0] as T1,
        results[1] as T2,
        results[2] as T3,
        results[3] as T4,
        results[4] as T5
    )
}

suspend inline fun <reified T1, reified T2, reified T3> fetchTriple(
    vararg methods: suspend () -> Any,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
): Triple<T1, T2, T3> {
    val results = methods.map { method ->
        CoroutineScope(coroutineContext).async {
            method()
        }
    }.awaitAll()
    return Triple(
        results[0] as T1,
        results[1] as T2,
        results[2] as T3,
    )
}


package com.flipperdevices.inappnotification.impl.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.time.Duration

class TimerTask(
    private val delayDuration: Duration,
    private val coroutineScope: CoroutineScope,
    private val block: suspend () -> Unit
) {

    private val lock = ReentrantLock()
    private var job: Job? = null

    fun start() {
        lock.withLock {
            if (job == null) {
                job = coroutineScope.launch {
                    launchTimer()
                }
            }
        }
    }

    fun shutdown() {
        lock.withLock {
            runBlocking {
                job?.cancelAndJoin()
            }
        }
    }

    private suspend fun CoroutineScope.launchTimer() {
        while (isActive) {
            try {
                block()
            } catch (blockExecutionError: Exception) {
            }
            delay(delayDuration)
        }
    }
}

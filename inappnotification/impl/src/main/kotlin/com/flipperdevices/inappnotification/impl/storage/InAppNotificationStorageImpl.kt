package com.flipperdevices.inappnotification.impl.storage

import android.util.Log
import com.markettwits.inappnotification.api.InAppNotificationListener
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.markettwits.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.util.Stack
import kotlin.time.Duration.Companion.seconds

private val TIMER_DELAY = 1.seconds


class InAppNotificationStorageImpl :
    InAppNotificationStorage {
    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val timerTask = TimerTask(
        delayDuration = TIMER_DELAY,
        coroutineScope = coroutineScope,
        block = { invalidate() }
    )

    private val pendingNotification = Stack<InAppNotification>()
    private var listener: InAppNotificationListener? = null
    private var nextNotificationTime = 0L

    override fun subscribe(listener: InAppNotificationListener) {
        if (this.listener != null) {
            unsubscribe()
        }
        this.listener = listener
        timerTask.start()
        invalidate()
    }

    override fun unsubscribe() {
        this.listener = null
        timerTask.shutdown()
    }

    @Synchronized
    override fun addNotification(notification: InAppNotification) {
        pendingNotification.push(notification)
        invalidate()
        Log.e("mt05", "-----------------------")
        Log.e("mt05", "add notification $notification")
    }

    @Synchronized
    private fun invalidate() {
        val notificationListener = listener ?: return

        val currentTime = System.currentTimeMillis()
        if (currentTime < nextNotificationTime) {
            return
        }

        if (pendingNotification.empty()) {
            return
        }

        val notificationToShown = pendingNotification.pop()
        runBlocking {
            notificationListener.onNewNotification(notificationToShown)
        }
        nextNotificationTime = System.currentTimeMillis()
    }
}

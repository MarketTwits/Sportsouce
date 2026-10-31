package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Intent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Label
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Message
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.selfupdater.models.UpdateTarget
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Job
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class InAppNotificationExecutor(
    private val selfUpdaterApi: SelfUpdaterApi,
    private val notificationStorage: InAppNotificationStorage,
    private val timeSource: TimeSource = TimeSource.Monotonic,
    private val checkInterval: Duration = 24.hours,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var resumeJob: Job? = null
    private var checkJob: Job? = null
    private var lastSuccessfulCheck: TimeMark? = null
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.CheckForUpdates -> checkForUpdates()
            Intent.PauseUpdate -> {
                resumeJob?.cancel()
                checkJob?.cancel()
            }
            Intent.ResumeUpdate -> {
                resumeJob?.cancel()
                resumeJob = scope.launch {
                    checkJob?.join()
                    // A check can still be finishing when the app returns to the foreground.
                    // Retry reconciliation after it releases the shared updater.
                    var result = selfUpdaterApi.resumeUpdate()
                    if (result == SelfUpdateResult.InProgress) {
                        selfUpdaterApi.getInProgressState().first { !it }
                        result = selfUpdaterApi.resumeUpdate()
                    }
                    if (result is SelfUpdateResult.SelfUpdateReady) offer(result)
                    if (result == SelfUpdateResult.NoUpdates && selfUpdaterApi.updateTarget() == UpdateTarget.Web) {
                        clearObsoleteWebOffers()
                        lastSuccessfulCheck = timeSource.markNow()
                    } else if (result == SelfUpdateResult.NoUpdates &&
                        (lastSuccessfulCheck?.elapsedNow()?.let { it >= checkInterval } ?: true)) {
                        performCheck()
                    }
                }
            }
            is Intent.OnNewNotification -> {
                dispatch(Message.Enqueue(intent.notification))
            }

            is Intent.OnNotificationHidden -> {
                if ((state() as? State.ShownNotification)?.notification == intent.notification) {
                    dispatch(Message.Dismiss(intent.notification))
                    (intent.notification as? InAppNotification.SelfUpdateReady)?.onDismiss?.invoke()
                }
            }

            is Intent.OpenNotificationScreen -> {
                if ((state() as? State.ShownNotification)?.notification != intent.notification) return
                when (val notification = intent.notification) {
                    is InAppNotification.SelfUpdateReady -> {
                        if (notification.readyToInstall) {
                            dispatch(Message.Dismiss(notification))
                            notification.action()
                        } else publish(Label.OpenUpdateAppScreen(mapSelfUpdateToShared(notification)))
                    }
                    InAppNotification.SelfUpdateStarted, is InAppNotification.SelfUpdateProgress ->
                        publish(Label.OpenUpdateAppScreen(NewAppVersion("", "")))
                    else -> Unit
                }
            }
        }
    }

    private fun checkForUpdates() {
        if (checkJob?.isActive == true) return
        checkJob = scope.launch { performCheck() }
    }

    private suspend fun performCheck() {
        var result = selfUpdaterApi.startCheckUpdate(false)
        if (result == SelfUpdateResult.InProgress) {
            selfUpdaterApi.getInProgressState().first { !it }
            result = selfUpdaterApi.startCheckUpdate(false)
        }
        if (result == SelfUpdateResult.NoUpdates || result is SelfUpdateResult.SelfUpdateReady ||
            result == SelfUpdateResult.Complete || result == SelfUpdateResult.Unsupported) {
            lastSuccessfulCheck = timeSource.markNow()
        }
        if (result is SelfUpdateResult.SelfUpdateReady) offer(result)
        if (result == SelfUpdateResult.NoUpdates && selfUpdaterApi.updateTarget() == UpdateTarget.Web) clearObsoleteWebOffers()
    }

    private fun clearObsoleteWebOffers() {
        val shown = state() as? State.ShownNotification ?: return
        (listOf(shown.notification) + shown.pending).filterIsInstance<InAppNotification.SelfUpdateReady>()
            .filter { it.informationalOnly && it.releaseId?.startsWith("web:") == true }
            .mapNotNull { it.releaseId }.distinct().forEach {
                dispatch(Message.Enqueue(InAppNotification.SelfUpdateCleared(it)))
            }
    }

    private fun offer(result: SelfUpdateResult.SelfUpdateReady) {
        if (!isAlreadyOffered(result)) {
            notificationStorage.addNotification(
                InAppNotification.SelfUpdateReady(
                    actualVersion = result.versionName,
                    description = result.description,
                    informationalOnly = result.informationalOnly,
                    releaseId = result.releaseId,
                    action = {},
                )
            )
        }
    }

    private fun isAlreadyOffered(result: SelfUpdateResult.SelfUpdateReady): Boolean {
        val shown = state() as? State.ShownNotification ?: return false
        val releaseId = result.releaseId ?: return false
        return (listOf(shown.notification) + shown.pending).any { notification ->
            notification is InAppNotification.SelfUpdateReady && !notification.readyToInstall &&
                notification.releaseId == releaseId && notification.informationalOnly == result.informationalOnly
        }
    }

    private fun mapSelfUpdateToShared(notification: InAppNotification.SelfUpdateReady): NewAppVersion =
        NewAppVersion(
            version = notification.actualVersion,
            description = notification.description,
            informationalOnly = notification.informationalOnly,
        )
}

package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.*
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class SelfUpdateExecutor(private val selfUpdaterApi: SelfUpdaterApi) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    private var screenResumed = false
    private var trackingDownload = false
    private var reconcileReadyOnResume = false
    private var progressJob: Job? = null
    private var readyResumeJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickUpdate -> {
                startCheck(true)
            }
            is Intent.OnClickCheck -> {
                startCheck(false)
            }
            Intent.OnClickInstall -> {
                if (!requestStarted) {
                    requestStarted = true
                    dispatch(Message.OpeningInstaller(true))
                    scope.launch {
                        try {
                            var result = selfUpdaterApi.installUpdate()
                            if (result == SelfUpdateResult.InProgress) {
                                selfUpdaterApi.getInProgressState().first { !it }
                                result = selfUpdaterApi.installUpdate()
                            }
                            when (result) {
                                SelfUpdateResult.ReadyToInstall, SelfUpdateResult.DesktopInstallerDownloaded -> Unit
                                SelfUpdateResult.NoUpdates -> dispatch(Message.NoUpdates)
                                is SelfUpdateResult.Error -> dispatch(Message.DownloadFailed(result.exception.message.orEmpty()))
                                else -> dispatch(Message.Status("Не удалось открыть установку. Попробуйте ещё раз."))
                            }
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (failure: Exception) {
                            dispatch(Message.DownloadFailed(failure.message.orEmpty()))
                        } finally {
                            requestStarted = false
                            dispatch(Message.OpeningInstaller(false))
                        }
                    }
                }
            }
            is Intent.ConsumedEvent -> dispatch(Message.ConsumedEvent)
            is Intent.OnClickGoBack -> { screenResumed = false; stopProgress(); readyResumeJob?.cancel(); publish(Label.GoBack) }
            Intent.ScreenResumed -> {
                screenResumed = true
                startProgress()
                if (reconcileReadyOnResume) reconcileReady()
            }
            Intent.ScreenPaused -> { screenResumed = false; stopProgress(); readyResumeJob?.cancel() }
        }
    }

    override fun executeAction(action: Unit) {
        scope.launch { selfUpdaterApi.downloadProgress().collect { dispatch(Message.TransferProgress(it)) } }
        action(state())
    }

    private fun action(state: State) {
        scope.launch {
            if (state.newAppInfo == null) {
                startCheck(false)
            } else {
                startCheck(false, restoreInfo = state.newAppInfo)
            }
        }
    }

    private fun startCheck(manual: Boolean, restoreInfo: NewAppVersion? = null) {
        if (requestStarted) return
        requestStarted = true
        readyResumeJob?.cancel()
        reconcileReadyOnResume = false
        trackingDownload = false
        stopProgress()
        dispatch(Message.Loading(true))
        scope.launch {
            try {
                suspend fun settled(request: suspend () -> SelfUpdateResult): SelfUpdateResult {
                    var result = request()
                    if (result == SelfUpdateResult.InProgress) {
                        selfUpdaterApi.getInProgressState().first { !it }
                        result = request()
                    }
                    return result
                }
                var result = settled {
                    if (restoreInfo != null) selfUpdaterApi.resumeUpdate()
                    else selfUpdaterApi.startCheckUpdate(manual)
                }
                // A saved notification can outlive the release it advertised. If
                // nothing is being restored, check the installed version again.
                if (restoreInfo != null && result == SelfUpdateResult.NoUpdates) {
                    result = settled { selfUpdaterApi.startCheckUpdate(false) }
                }
                restoreReleaseInfo()
                when (result) {
                    is SelfUpdateResult.Complete -> {
                        dispatch(Message.DownloadStarted)
                    }

                    is SelfUpdateResult.Error -> {
                        dispatch(Message.DownloadFailed(message = result.exception.message.toString()))
                    }

                    is SelfUpdateResult.InProgress -> {
                        dispatch(Message.DownloadFailed("Update check is busy"))
                    }

                    SelfUpdateResult.DownloadInProgress -> {
                        dispatch(Message.DownloadPending(false))
                        trackingDownload = true
                        startProgress()
                    }
                    SelfUpdateResult.InteractiveUpdateInProgress -> {
                        dispatch(Message.Status("Открыто окно обновления. Следуйте указаниям в нём."))
                        trackingDownload = true
                        startProgress()
                    }
                    is SelfUpdateResult.DownloadProgress -> {
                        dispatch(Message.DownloadPending(false, result.percent, result.paused, showDialog = false))
                        trackingDownload = true
                        startProgress()
                    }
                    SelfUpdateResult.ReadyToInstall -> { reconcileReadyOnResume = true; dispatch(Message.DownloadPending(true)) }
                    SelfUpdateResult.DesktopInstallerDownloaded -> { reconcileReadyOnResume = true; dispatch(Message.DownloadPending(true)) }
                    SelfUpdateResult.Unsupported -> dispatch(Message.Status(unsupportedMessage()))

                    is SelfUpdateResult.NoUpdates -> dispatch(Message.NoUpdates)

                    is SelfUpdateResult.SelfUpdateReady -> {
                        dispatch(
                            Message.NewAppAvailable(
                                NewAppVersion(
                                    result.versionName,
                                    result.description,
                                    result.informationalOnly,
                                )
                            )
                        )
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                dispatch(Message.DownloadFailed(failure.message.orEmpty()))
            } finally {
                requestStarted = false
            }
        }
    }

    private fun unsupportedMessage(): String = when (selfUpdaterApi.updateTarget()) {
        com.markettwits.selfupdater.models.UpdateTarget.Web -> "В локальной веб-сборке проверка обновлений недоступна."
        com.markettwits.selfupdater.models.UpdateTarget.Desktop -> "Для этой системы или архитектуры нет совместимого установщика в релизе."
        else -> "Для этого устройства пакет обновления недоступен."
    }

    private fun restoreReleaseInfo() {
        if (state().newAppInfo?.version.isNullOrBlank()) {
            selfUpdaterApi.pendingRelease()?.let {
                dispatch(Message.ReleaseInfo(NewAppVersion(it.versionName, it.description, it.informationalOnly)))
            }
        }
    }

    private var requestStarted = false

    private fun startProgress() {
        if (!screenResumed || !trackingDownload || progressJob?.isActive == true) return
        progressJob = scope.launch {
            while (screenResumed && trackingDownload) {
                delay(2_000)
                restoreReleaseInfo()
                when (val result = selfUpdaterApi.resumeUpdate()) {
                    is SelfUpdateResult.DownloadProgress -> dispatch(Message.DownloadPending(false, result.percent, result.paused, showDialog = false))
                    SelfUpdateResult.DownloadInProgress, SelfUpdateResult.InteractiveUpdateInProgress, SelfUpdateResult.InProgress -> Unit
                    SelfUpdateResult.ReadyToInstall -> {
                        trackingDownload = false
                        reconcileReadyOnResume = true
                        dispatch(Message.DownloadPending(true))
                    }
                    SelfUpdateResult.DesktopInstallerDownloaded -> {
                        trackingDownload = false
                        reconcileReadyOnResume = true
                        dispatch(Message.DownloadPending(true))
                    }
                    SelfUpdateResult.Unsupported -> {
                        trackingDownload = false
                        dispatch(Message.Status(unsupportedMessage()))
                    }
                    is SelfUpdateResult.SelfUpdateReady -> {
                        trackingDownload = false
                        dispatch(Message.NewAppAvailable(NewAppVersion(result.versionName, result.description, result.informationalOnly)))
                    }
                    SelfUpdateResult.NoUpdates -> { trackingDownload = false; dispatch(Message.NoUpdates) }
                    is SelfUpdateResult.Error -> { trackingDownload = false; dispatch(Message.DownloadFailed(result.exception.message.orEmpty())) }
                    else -> { trackingDownload = false; dispatch(Message.Status("Состояние обновления изменилось. Проверьте его ещё раз.")) }
                }
            }
        }
    }

    private fun stopProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun reconcileReady() {
        if (readyResumeJob?.isActive == true || requestStarted) return
        readyResumeJob = scope.launch {
            var result = selfUpdaterApi.resumeUpdate()
            if (result == SelfUpdateResult.InProgress) {
                selfUpdaterApi.getInProgressState().first { !it }
                result = selfUpdaterApi.resumeUpdate()
            }
            when (result) {
                SelfUpdateResult.ReadyToInstall, SelfUpdateResult.DesktopInstallerDownloaded -> dispatch(Message.DownloadPending(true, showDialog = false))
                SelfUpdateResult.NoUpdates -> { reconcileReadyOnResume = false; dispatch(Message.NoUpdates) }
                SelfUpdateResult.Unsupported -> { reconcileReadyOnResume = false; dispatch(Message.Status(unsupportedMessage())) }
                is SelfUpdateResult.Error -> { dispatch(Message.DownloadFailed(result.exception.message.orEmpty())) }
                is SelfUpdateResult.DownloadProgress -> {
                    reconcileReadyOnResume = false
                    trackingDownload = true
                    dispatch(Message.DownloadPending(false, result.percent, result.paused, showDialog = false))
                    startProgress()
                }
                SelfUpdateResult.DownloadInProgress -> {
                    reconcileReadyOnResume = false
                    trackingDownload = true
                    dispatch(Message.DownloadPending(false, showDialog = false))
                    startProgress()
                }
                else -> Unit
            }
        }
    }
}

package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State

object SelfUpdateReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loading -> if (!msg.isLoading) copy(isLoading = false) else copy(
                isLoading = true, isFailed = false, isSuccess = false, pendingMessage = null,
                readyToInstall = false, downloadActive = false, downloadPaused = false,
                downloadPercent = null, transferPercent = null,
            )
            is Message.OpeningInstaller -> copy(installerOpening = msg.opening)
            is Message.ReleaseInfo -> copy(newAppInfo = msg.info)
            is Message.TransferProgress -> copy(transferPercent = msg.percent)
            is Message.DownloadFailed -> copy(
                downloadPaused = false,
                transferPercent = null,
                isLoading = false,
                downloadActive = false,
                readyToInstall = false,
                downloadPercent = null,
                isFailed = true,
                isSuccess = false,
                message = "Не удалось проверить или подготовить обновление. Попробуйте позже.",
                pendingMessage = "Не удалось проверить или подготовить обновление. Вернитесь к проверке позже.",
            )

            is Message.DownloadStarted -> copy(
                readyToInstall = false,
                downloadPaused = false,
                transferPercent = null,
                isLoading = false,
                downloadActive = false,
                downloadPercent = null,
                isSuccess = true,
                isFailed = false,
                message = "Обновление запущено. Следуйте дальнейшим указаниям на экране.",
                pendingMessage = "Обновление запущено. Следуйте дальнейшим указаниям на экране.",
            )

            is Message.DownloadPending -> copy(
                isLoading = false,
                isSuccess = msg.showDialog,
                isFailed = false,
                downloadActive = !msg.ready,
                readyToInstall = msg.ready,
                downloadPercent = if (msg.ready) null else msg.percent,
                downloadPaused = !msg.ready && msg.paused,
                message = downloadStatus(msg),
                pendingMessage = downloadStatus(msg),
            )

            is Message.Status -> copy(downloadPaused = false, transferPercent = null, isLoading = false, isSuccess = false, isFailed = false, downloadActive = false, readyToInstall = false, downloadPercent = null, pendingMessage = msg.text)

            is Message.ConsumedEvent -> copy(isLoading = false, isSuccess = false, isFailed = false)
            is Message.NoUpdates -> copy(
                downloadPaused = false,
                transferPercent = null,
                isLoading = false,
                isSuccess = false,
                isFailed = false,
                updatesAvailable = false,
                newAppInfo = null,
                pendingMessage = null,
                readyToInstall = false,
                downloadActive = false,
                downloadPercent = null,
            )

            is Message.NewAppAvailable -> copy(
                downloadPaused = false,
                transferPercent = null,
                isLoading = false,
                isSuccess = false,
                isFailed = false,
                pendingMessage = null,
                readyToInstall = false,
                downloadActive = false,
                downloadPercent = null,
                updatesAvailable = true,
                newAppInfo = msg.newAppInfo
            )
        }
    }

    private fun downloadStatus(msg: Message.DownloadPending): String = when {
        msg.ready -> "Обновление готово к установке."
        msg.percent != null && msg.paused -> "Загрузка приостановлена: ${msg.percent}%. Проверьте состояние позже."
        msg.paused -> "Загрузка приостановлена. Проверьте состояние позже."
        msg.percent != null -> "Обновление загружается: ${msg.percent}%."
        else -> "Обновление загружается. Можно продолжать пользоваться приложением."
    }
}

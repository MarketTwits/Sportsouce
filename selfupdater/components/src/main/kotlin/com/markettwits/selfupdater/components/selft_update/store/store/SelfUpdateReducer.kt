package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message

object SelfUpdateReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loading -> copy(isLoading = msg.isLoading)

            is Message.DownloadFailed -> copy(
                isLoading = false,
                isFailed = true,
                isSuccess = false,
                message = msg.message
            )

            is Message.DownloadStarted -> copy(
                isLoading = false,
                isSuccess = true,
                isFailed = false,
                message = "Обновление успешно запущено,дождитесь загрузки"
            )

            is Message.ConsumedEvent -> copy(isLoading = false, isSuccess = false, isFailed = false)
        }
    }
}
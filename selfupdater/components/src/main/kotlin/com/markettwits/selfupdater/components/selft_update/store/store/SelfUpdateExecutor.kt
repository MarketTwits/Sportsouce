package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Label
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message
import kotlinx.coroutines.launch

class SelfUpdateExecutor(private val selfUpdaterApi: SelfUpdaterApi) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickUpdate -> {
                update()
            }

            is Intent.ConsumedEvent -> dispatch(Message.ConsumedEvent)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        progress()
    }

    private fun progress() {
        scope.launch {
            selfUpdaterApi.getInProgressState().collect {
                dispatch(Message.Loading(it))
            }
        }
    }

    private fun update() {
        scope.launch {
            when (val result = selfUpdaterApi.startCheckUpdate(true)) {
                is SelfUpdateResult.COMPLETE -> {
                    dispatch(Message.DownloadStarted)
                }

                is SelfUpdateResult.ERROR -> {
                    dispatch(Message.DownloadFailed(message = result.exception.message.toString()))
                }

                is SelfUpdateResult.IN_PROGRESS -> {
                    dispatch(Message.Loading(true))
                }

                is SelfUpdateResult.NO_UPDATES -> {}
            }
        }
    }
}

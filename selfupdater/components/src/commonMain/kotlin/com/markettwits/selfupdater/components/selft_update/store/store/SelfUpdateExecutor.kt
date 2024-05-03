package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Label
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Message
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State
import kotlinx.coroutines.launch

class SelfUpdateExecutor(private val selfUpdaterApi: SelfUpdaterApi) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickUpdate -> {
                startCheck(true)
            }
            is Intent.ConsumedEvent -> dispatch(Message.ConsumedEvent)
            is Intent.OnClickGoBack -> publish(Label.GoBack)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        observeProgress()
        action(getState())
    }

    private fun observeProgress() {
        scope.launch {
            selfUpdaterApi.getInProgressState().collect {
                dispatch(Message.Loading(it))
            }
        }
    }

    private fun action(state: State) {
        scope.launch {
            if (state.newAppInfo == null) {
                dispatch(Message.Loading(true))
                startCheck(false)
            } else {
                dispatch(Message.NewAppAvailable(state.newAppInfo))
            }
        }
    }

    private fun startCheck(manual: Boolean) {
        scope.launch {
            when (val result = selfUpdaterApi.startCheckUpdate(manual)) {
                is SelfUpdateResult.Complete -> {
                    dispatch(Message.DownloadStarted)
                }

                is SelfUpdateResult.Error -> {
                    dispatch(Message.DownloadFailed(message = result.exception.message.toString()))
                }

                is SelfUpdateResult.InProgress -> {
                    dispatch(Message.Loading(true))
                }

                is SelfUpdateResult.NoUpdates -> {
                    dispatch(Message.NoUpdates)
                }

                is SelfUpdateResult.SelfUpdateReady -> {
                    dispatch(
                        Message.NewAppAvailable(
                            NewAppVersion(
                                result.versionName,
                                result.description
                            )
                        )
                    )
                }
            }
        }
    }
}

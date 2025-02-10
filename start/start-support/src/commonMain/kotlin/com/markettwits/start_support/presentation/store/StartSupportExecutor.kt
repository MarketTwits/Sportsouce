package com.markettwits.start_support.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.start_support.domain.StartSupportUseCase
import com.markettwits.start_support.presentation.store.StartSupportStore.Intent
import com.markettwits.start_support.presentation.store.StartSupportStore.Label
import com.markettwits.start_support.presentation.store.StartSupportStore.Message
import com.markettwits.start_support.presentation.store.StartSupportStore.State
import kotlinx.coroutines.launch

internal class StartSupportExecutor(
    private val intentAction: IntentAction,
    private val useCase: StartSupportUseCase,
    private val startId: Int
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnChangeValue -> dispatch(Message.OnValueChanged(intent.value))
            is Intent.OnClickSupport -> launch(state().cost)
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
        }
    }

    private fun launch(cost: String) {
        scope.launch {
            dispatch(Message.Loading)
            useCase.donation(startId, cost).fold(
                onSuccess = {
                    intentAction.openWebPage(it)
                },
                onFailure = { dispatch(Message.Failure(it.message.toString())) }
            )
        }
    }
}

package com.markettwits.club.info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.club.info.domain.ClubInfoRepository
import com.markettwits.club.info.presentation.store.ClubInfoStore.Intent
import com.markettwits.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.club.info.presentation.store.ClubInfoStore.State
import com.markettwits.club.info.presentation.store.ClubInfoStore.Message
import kotlinx.coroutines.launch

internal class ClubInfoExecutor(
    private val repository: ClubInfoRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun launch() {
        scope.launch {
            repository.clubInfo().onSuccess {
                dispatch(Message.Loaded(it))
            }.onFailure {
                println(it)
            }
        }
    }
}

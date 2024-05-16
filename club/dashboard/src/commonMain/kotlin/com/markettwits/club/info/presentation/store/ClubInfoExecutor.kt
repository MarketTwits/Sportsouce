package com.markettwits.club.info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.presentation.store.ClubInfoStore.Intent
import com.markettwits.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.club.info.presentation.store.ClubInfoStore.Message
import com.markettwits.club.info.presentation.store.ClubInfoStore.State
import kotlinx.coroutines.launch

internal class ClubInfoExecutor(
    private val repository: ClubRepository,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(false, getState().info)
    }

    private fun launch(forced: Boolean, items: List<ClubInfo>) {
        if (items.isEmpty() || forced) {
            scope.launch {
                repository.clubInfo().onSuccess {
                    dispatch(Message.Loaded(it))
                }.onFailure {
                    println(it)
                }
            }
        } else {
            dispatch(Message.Loaded(items))
        }
    }
}

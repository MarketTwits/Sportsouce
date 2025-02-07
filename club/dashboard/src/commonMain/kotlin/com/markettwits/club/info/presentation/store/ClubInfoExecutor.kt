package com.markettwits.club.info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.presentation.store.ClubInfoStore.Intent
import com.markettwits.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.club.info.presentation.store.ClubInfoStore.Message
import com.markettwits.club.info.presentation.store.ClubInfoStore.State
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import kotlinx.coroutines.launch

internal class ClubInfoExecutor(
    private val repository: ClubRepository,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
        }
    }

    override fun executeAction(action: Unit) {
        launch(false, state().info)
    }

    private fun launch(forced: Boolean, items: List<ClubInfo>) {
        if (items.isEmpty() || forced) {
            scope.launch {
                repository.clubInfo().onSuccess {
                    dispatch(Message.Loaded(it))
                }
            }
        } else {
            dispatch(Message.Loaded(items))
        }
    }
}

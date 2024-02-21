package com.markettwits.profile.presentation.component.authorized.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepository
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Message
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.State
import kotlinx.coroutines.launch

class AuthorizedProfileExecutor(private val repository: AuthorizedProfileRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            else -> {}
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            repository.profile().fold(
                onSuccess = { dispatch(Message.LoadingSuccess(it)) },
                onFailure = { dispatch(Message.LoadingFailed(it.message.toString())) }
            )
        }
    }
}

package com.markettwits.profile.presentation.component.authorized.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepository
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Message
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AuthorizedProfileExecutor(private val repository: AuthorizedProfileRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            Intent.Retry -> launch(true)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(false)
    }

    private fun launch(forced: Boolean) {
        scope.launch {
            dispatch(Message.Loading)
            repository.profile(forced)
                .catch { dispatch(Message.LoadingFailed(it.message.toString())) }
                .collect { dispatch(Message.LoadingSuccess(it)) }
        }
    }
}

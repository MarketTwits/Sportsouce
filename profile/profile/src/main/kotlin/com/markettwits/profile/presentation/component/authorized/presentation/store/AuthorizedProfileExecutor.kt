package com.markettwits.profile.presentation.component.authorized.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.profile.presentation.component.authorized.domain.UserProfileInteractor
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Message
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthorizedProfileExecutor(private val interactor: UserProfileInteractor) :
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
            interactor.userProfile(forced)
                .onStart { dispatch(Message.Loading) }
                .catch { dispatch(Message.LoadingFailed(networkExceptionHandler(it).message.toString())) }
                .collect { dispatch(Message.LoadingSuccess(it)) }
        }
    }
}

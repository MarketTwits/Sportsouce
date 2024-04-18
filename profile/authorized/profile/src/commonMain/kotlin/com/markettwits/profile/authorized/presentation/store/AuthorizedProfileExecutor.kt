package com.markettwits.profile.authorized.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.profile.authorized.domain.UserProfileInteractor
import com.markettwits.profile.authorized.domain.UserSocialNetworkIntent
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.Message
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthorizedProfileExecutor(
    private val interactor: UserProfileInteractor,
    private val action: IntentAction
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Retry -> launch(true)
            is Intent.OnClickUserSocialNetwork -> handleUserSocialNetwork(intent.intent)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(false)
    }

    private fun handleUserSocialNetwork(intent: UserSocialNetworkIntent) {
        when (intent) {
            is UserSocialNetworkIntent.Link -> action.openWebPage(intent.value)
            is UserSocialNetworkIntent.Phone -> action.openPhone(intent.value)
        }
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

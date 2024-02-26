package com.markettwits.edit_profile.edit_social_network.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork
import com.markettwits.edit_profile.edit_social_network.domain.interactor.ProfileSocialNetworkInteractor
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Intent
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Label
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Message
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.State
import kotlinx.coroutines.launch

class EditProfileSocialNetworkExecutor(private val interactor: ProfileSocialNetworkInteractor) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnCLickUpdate -> getState().data?.let { update(it) }
            is Intent.UpdateState -> dispatch(Message.UpdateFiled(intent.userSocialNetwork))
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun update(userSocialNetwork: UserSocialNetwork) {
        scope.launch {
            dispatch(Message.IsLoading)
            interactor.send(userSocialNetwork).fold(
                onSuccess = {
                    dispatch(Message.UpdateSuccess("Данные успешно обновлены"))
                },
                onFailure = {
                    dispatch(Message.UpdateFailed(it.message.toString()))
                }
            )
        }
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.IsLoading)
            interactor.fetch().fold(
                onFailure = {
                    dispatch(Message.IsFailed(it.message.toString()))
                },
                onSuccess = {
                    dispatch(Message.IsLoaded(it))
                }
            )
        }
    }
}

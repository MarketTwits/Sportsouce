package com.markettwits.sportsouce.edit_profile.social_network.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.edit_profile.social_network.domain.UserSocialNetwork
import com.markettwits.sportsouce.edit_profile.social_network.domain.interactor.ProfileSocialNetworkInteractor
import com.markettwits.sportsouce.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore.*
import kotlinx.coroutines.launch

class EditProfileSocialNetworkExecutor(private val interactor: ProfileSocialNetworkInteractor) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnCLickUpdate -> state().data?.let { update(it) }
            is Intent.UpdateState -> dispatch(Message.UpdateFiled(intent.userSocialNetwork))
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
        }
    }

    override fun executeAction(action: Unit) {
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
                    dispatch(Message.UpdateFailed(it.mapToSauceError().mapToString()))
                }
            )
        }
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.IsLoading)
            interactor.fetch().fold(
                onFailure = {
                    dispatch(Message.IsFailed(it.mapToSauceError().mapToString()))
                },
                onSuccess = {
                    dispatch(Message.IsLoaded(it))
                }
            )
        }
    }
}

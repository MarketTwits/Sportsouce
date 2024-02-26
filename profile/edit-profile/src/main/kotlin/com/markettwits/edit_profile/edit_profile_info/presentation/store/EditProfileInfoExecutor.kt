package com.markettwits.edit_profile.edit_profile_info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Intent
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Label
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Message
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.State
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch

class EditProfileInfoExecutor(private val repository: EditProfileInfoRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickUpdate -> getState().userData?.let { update(it) }
            is Intent.UpdateState -> dispatch(Message.UpdateFiled(intent.userData))
            is Intent.OnConsumedEvent -> dispatch(
                Message.OnConsumedEvent
            )
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun update(userData: UserData) {
        scope.launch {
            dispatch(Message.IsLoading)
            repository.send(userData).fold(
                onSuccess = {
                    dispatch(Message.UpdateSuccess("Данные профиля успешно обновлены"))
                },
                onFailure = {
                    val message =
                        if (it is ClientRequestException)
                            it.response.body<AuthErrorResponse>().message
                        else it.message.toString()
                    dispatch(Message.UpdateFailed(message))
                }
            )
        }
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.IsLoading)
            repository.fetch().fold(
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

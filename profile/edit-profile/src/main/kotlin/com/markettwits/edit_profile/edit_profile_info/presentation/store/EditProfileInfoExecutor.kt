package com.markettwits.edit_profile.edit_profile_info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Intent
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Label
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Message
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditProfileInfoExecutor(private val repository: EditProfileInfoRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickUpdate -> getState().userData?.let { update(it) }
            is Intent.UpdateState -> dispatch(Message.UpdateFiled(intent.userData))
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.Retry -> launch()
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
                    dispatch(Message.UpdateFailed(networkExceptionHandler(it).message.toString()))
                }
            )
        }
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.IsLoading)
            repository.fetch()
                .catch {
                    dispatch(Message.IsFailed(networkExceptionHandler(it).message.toString()))
                }
                .collect {
                    dispatch(Message.IsLoaded(it))
                }
        }
    }
}

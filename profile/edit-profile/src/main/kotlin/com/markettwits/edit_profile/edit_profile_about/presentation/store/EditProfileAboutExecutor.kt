package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutRepository
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Message
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State
import kotlinx.coroutines.launch

class EditProfileAboutExecutor(private val repository: EditProfileAboutRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnValueChanged -> handleTextFiled(getState(), intent.value)
            Intent.Apply -> apply(getState())
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            repository.fetch().fold(
                onSuccess = {
                    dispatch(Message.LoadedAbout(State.FiledState(it)))
                },
                onFailure = {
                    dispatch(Message.UpdateFailed(it.message.toString()))
                }
            )
        }
    }

    private fun apply(state: State) {
        scope.launch {
            if (!state.isLoading || !state.fieldStat.isError)
                dispatch(Message.Loading)
            repository.send(state.fieldStat.value).fold(
                onSuccess = {
                    dispatch(Message.UpdateSuccess)
                },
                onFailure = {
                    dispatch(Message.UpdateFailed(it.message.toString()))
                }
            )
        }
    }

    private fun handleTextFiled(state: State, value: String) {
        val error = value.length > 120
        dispatch(
            Message.UpdateTexField(
                state.fieldStat.copy(
                    value = value,
                    isError = error
                )
            )
        )
    }
}

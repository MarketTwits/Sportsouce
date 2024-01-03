package com.markettwits.profile.presentation.component.edit_profile.presentation

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.component.edit_profile.data.EditProfileDataStore
import com.markettwits.profile.presentation.component.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer


class EditProfileComponent(
    context: ComponentContext,
    private val userId: Int,
    private val mapper: RemoteUserToEditProfileMapper,
    private val service: EditProfileDataStore,
    private val goBack: () -> Unit,
) : EditProfile, ComponentContext by context {

    override val state: MutableValue<EditProfileUiState> = MutableValue(
        stateKeeper.consume(PAGE_STATE_KEY, EditProfileUiState.serializer())
            ?: EditProfileUiState.Loading()
    )
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        stateKeeper.register(
            key = PAGE_STATE_KEY,
            EditProfileUiState.serializer()
        ) { state.value }

        if (state.value.data.isEmpty()) {
            Log.d("mt05", "startdata " + state.value.data.toString())
            state.value = EditProfileUiState.Loading()
            fetchProfile()
        }
    }

    override fun pop() = goBack()
    override fun saveChanges() {
        scope.launch {
            state.value = EditProfileUiState.LoadingChanges(data = state.value.data)
            val user = service.changeProfileInfo(state.value.data)
            handleSaveChanges(user)
        }
    }

    override fun messageHasBeenShowed() {
        // state.value = EditProfileUiState.Error("", false)
    }

    private fun handleSaveChanges(editProfileUiState: EditProfileUiState) {
        when (editProfileUiState) {
            is EditProfileUiState.Error -> {
                Log.e("mt05", editProfileUiState.message)
                state.value =
                    EditProfileUiState.Error(data = state.value.data, message = editProfileUiState.message)
                sendEventSync(EditProfileEvent.ShowError(editProfileUiState.message))
            }

            is EditProfileUiState.Base -> {
                state.value =
                    EditProfileUiState.Base(data = editProfileUiState.data, isLoading = false)
                sendEventSync(EditProfileEvent.ShowSuccess("Данные успешно обновлены"))
            }

            is EditProfileUiState.Loading -> {
                state.value = EditProfileUiState.Loading()
            }
            else -> {}
        }
    }

    private fun fetchProfile() {
        scope.launch {
            state.value = EditProfileUiState.Loading()
            state.value = service.profile()
        }
    }

    override fun obtainTextFiled(value: EditProfileUiPage) {
        updatePage(value)
    }

    private fun <T : EditProfileUiPage> updatePage(updatedUser: T) {
        val updatedList = state.value.data.map { page ->
            when {
                page::class == updatedUser::class -> updatedUser
                else -> page
            }
        }
        state.value = EditProfileUiState.Base(data = updatedList)
    }

    private val _event = Channel<EditProfileEvent>()
    override val events = _event.receiveAsFlow().shareIn(scope, SharingStarted.Lazily)
    protected fun sendEventSync(event: EditProfileEvent) = scope.launch { _event.send(event) }

    private companion object {
        const val PAGE_STATE_KEY = "PAGE_STATE"
    }
}
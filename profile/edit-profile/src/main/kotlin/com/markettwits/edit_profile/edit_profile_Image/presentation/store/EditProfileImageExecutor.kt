package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_profile_Image.data.EditProfileImageRepository
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Message
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State
import kotlinx.coroutines.launch
import java.io.File

class EditProfileImageExecutor(private val repository: EditProfileImageRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.UpdateImage -> uploadImage(intent.file)
        }
    }

    private fun uploadImage(file: File) {
        scope.launch {
            dispatch(Message.ShowLoading)
            repository.send(file).fold(
                onSuccess = { dispatch(Message.ShowSuccess) },
                onFailure = { dispatch(Message.ShowError(it.message.toString())) }
            )
        }
    }
}

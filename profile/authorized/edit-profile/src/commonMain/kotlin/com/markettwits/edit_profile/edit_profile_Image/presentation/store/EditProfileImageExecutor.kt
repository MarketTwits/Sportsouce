package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.edit_profile.edit_profile_Image.data.EditProfileImageRepository
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Message
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State
import kotlinx.coroutines.launch

class EditProfileImageExecutor(private val repository: EditProfileImageRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.UpdateImage -> uploadImage(intent.data, intent.lastModified)
        }
    }

    private fun uploadImage(data: ByteArray, lastModified: Long) {
        scope.launch {
            dispatch(Message.ShowLoading)
            repository.send(data, lastModified).fold(
                onSuccess = { dispatch(Message.ShowSuccess) },
                onFailure = {
                    println(it)
                    dispatch(Message.ShowError(networkExceptionHandler(it).message.toString()))
                }
            )
        }
    }
}

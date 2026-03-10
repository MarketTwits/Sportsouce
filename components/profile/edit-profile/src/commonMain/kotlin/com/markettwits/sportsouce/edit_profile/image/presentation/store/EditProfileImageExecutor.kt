package com.markettwits.sportsouce.edit_profile.image.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.edit_profile.image.data.EditProfileImageRepository
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore.*
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
                    dispatch(Message.ShowError(it.mapToSauceError().mapToString()))
                }
            )
        }
    }
}

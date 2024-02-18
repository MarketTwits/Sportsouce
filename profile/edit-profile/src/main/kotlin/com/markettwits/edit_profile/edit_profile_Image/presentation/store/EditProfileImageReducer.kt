package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Message
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

object EditProfileImageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ShowError -> State(isFailed = true, message = msg.message)
            is Message.ShowLoading -> State(isLoading = true)
            is Message.ShowSuccess -> State(
                isSuccess = true,
                message = "Фото профиля успешно обновлено"
            )
        }
    }
}
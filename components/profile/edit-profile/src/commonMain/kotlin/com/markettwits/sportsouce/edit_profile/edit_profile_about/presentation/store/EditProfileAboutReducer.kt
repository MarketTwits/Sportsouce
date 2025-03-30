package com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Message
import com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

object EditProfileAboutReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loading -> copy(isLoading = true)
            is Message.UpdateFailed -> copy(
                isLoading = false,
                isSuccess = false,
                isError = true,
                message = msg.message
            )

            is Message.UpdateSuccess -> copy(
                isLoading = false,
                isError = false,
                isSuccess = true,
                message = "Данные профиля успешно обновлены"
            )

            is Message.UpdateTexField -> copy(fieldStat = msg.filedState)
            is Message.LoadedAbout -> copy(isLoading = false, fieldStat = msg.fieldState)
        }
    }
}
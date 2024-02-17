package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Message
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

object EditProfileImageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}
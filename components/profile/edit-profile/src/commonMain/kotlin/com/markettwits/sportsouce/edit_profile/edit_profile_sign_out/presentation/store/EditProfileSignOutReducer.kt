package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Message
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.State

object EditProfileSignOutReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}
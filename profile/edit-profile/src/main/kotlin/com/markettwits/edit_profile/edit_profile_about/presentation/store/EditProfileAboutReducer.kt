package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Message
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

object EditProfileAboutReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}
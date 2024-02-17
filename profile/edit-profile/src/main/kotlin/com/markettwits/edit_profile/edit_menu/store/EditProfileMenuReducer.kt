package com.markettwits.edit_profile.edit_menu.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.Message
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.State

object EditProfileMenuReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}
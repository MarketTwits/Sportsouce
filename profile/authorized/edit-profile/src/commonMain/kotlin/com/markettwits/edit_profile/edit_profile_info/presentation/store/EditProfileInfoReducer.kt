package com.markettwits.edit_profile.edit_profile_info.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Message
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.State

object EditProfileInfoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.IsFailed -> State(
                isLoading = false,
                isError = true,
                message = msg.message
            )

            is Message.IsLoaded -> State(
                userData = msg.userDataContent.user,
                teams = msg.userDataContent.teams,
                cities = msg.userDataContent.cities
            )

            is Message.IsLoading -> copy(isLoading = true)
            is Message.UpdateFailed -> copy(
                isLoading = false, event = triggered(
                    EventContent(false, msg.message)
                )
            )

            is Message.UpdateSuccess -> copy(
                isLoading = false, event = triggered(
                    EventContent(true, msg.message)
                )
            )

            is Message.UpdateFiled -> {
                copy(userData = msg.userData)
            }

            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}
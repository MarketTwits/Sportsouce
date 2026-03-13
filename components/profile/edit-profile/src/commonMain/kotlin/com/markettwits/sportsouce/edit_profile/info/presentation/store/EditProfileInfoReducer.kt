package com.markettwits.sportsouce.edit_profile.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore.Message
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore.State

object EditProfileInfoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.IsFailed -> State(
                isLoading = false,
                error = msg.sauceError,
            )

            is Message.IsLoaded -> State(
                isLoading = false,
                error = null,
                userData = msg.userDataContent.user,
                teams = msg.userDataContent.teams,
                cities = msg.userDataContent.cities
            )

            is Message.IsLoading -> copy(isLoading = true, error = null)
            is Message.UpdateFailed -> copy(
                isLoading = false, event = triggered(
                    EventContent(false, msg.sauceError.mapToString())
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

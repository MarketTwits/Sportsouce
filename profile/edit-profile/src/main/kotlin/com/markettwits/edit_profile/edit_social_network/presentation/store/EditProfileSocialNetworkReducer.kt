package com.markettwits.edit_profile.edit_social_network.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Message
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.State

object EditProfileSocialNetworkReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.IsFailed -> State(isLoading = false, isError = true, message = msg.message)
            is Message.IsLoaded -> State(data = msg.userSocialNetwork)
            is Message.IsLoading -> copy(isLoading = true)
            is Message.UpdateFailed -> copy(
                isLoading = false,
                event = triggered(EventContent(false, msg.message))
            )

            is Message.UpdateSuccess -> copy(
                isLoading = false,
                event = triggered(EventContent(true, msg.message))
            )

            is Message.UpdateFiled -> copy(data = msg.userSocialNetwork)
            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}
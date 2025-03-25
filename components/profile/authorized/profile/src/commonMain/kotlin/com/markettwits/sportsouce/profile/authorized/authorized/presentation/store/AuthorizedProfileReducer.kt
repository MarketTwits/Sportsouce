package com.markettwits.sportsouce.profile.authorized.authorized.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore.Message
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore.State

object AuthorizedProfileReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loading -> copy(isLoading = true, isError = false)
            is Message.LoadingFailed -> copy(
                isLoading = false,
                isError = true,
                message = msg.message
            )
            is Message.LoadingSuccess -> copy(user = msg.user, isLoading = false, isError = false)
        }
    }
}
package com.markettwits.start.register.presentation.registration.registration.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Message
import com.markettwits.start.register.presentation.registration.registration.store.StartRegistrationPageStore.State

object StartRegistrationPageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateStages -> copy(
                stages = msg.stagePage,
                pagesState = copy().pagesState.copy(isLoading = false)
            )
            is Message.UpdateEvent -> copy(eventWithContent = msg.eventContent)

            is Message.UpdateStageError -> copy(
                pagesState = copy().pagesState.copy(
                    isLoading = false, error = msg.error
                )
            )

            is Message.UpdateStageLoading -> copy(pagesState = copy().pagesState.copy(isLoading = true))
        }
    }
}
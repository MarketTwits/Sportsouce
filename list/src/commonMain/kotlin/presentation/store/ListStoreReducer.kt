package presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import presentation.store.ListStore.State
import presentation.store.ListStore.Message

object ListStoreReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failure -> State(message = msg.message, isError = true)
            is Message.Loading -> copy(isLoading = true)
            is Message.Success -> State(data = msg.data)
        }
    }
}
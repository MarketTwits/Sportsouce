package detail.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import detail.presentation.store.UserDetailStore.State
import detail.presentation.store.UserDetailStore.Message

object UserDetailStoreReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}
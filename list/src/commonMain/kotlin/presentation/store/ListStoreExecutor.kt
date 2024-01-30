package presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import data.RandomListUserRepository
import kotlinx.coroutines.launch
import presentation.store.ListStore.Intent
import presentation.store.ListStore.Label
import presentation.store.ListStore.State
import presentation.store.ListStore.Message

class ListStoreExecutor(private val repository: RandomListUserRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Launch -> launch(intent.forced)
            is Intent.OnClickItem -> publish(ListStore.Label.OnClickItem(intent.user))
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(false)
    }

    private fun launch(forced : Boolean) = scope.launch {
        dispatch(ListStore.Message.Loading)
        repository.randomUser(forced).fold(
            onSuccess = {
                dispatch(ListStore.Message.Success(it))
            },
            onFailure = {
                dispatch(ListStore.Message.Failure(it.message.toString()))
            }
        )
    }
}

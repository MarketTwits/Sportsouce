package presentation.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.random_user.RandomUser
import presentation.store.ListStore.Intent
import presentation.store.ListStore.Label
import presentation.store.ListStore.State

interface ListStore : Store<Intent, State, Label> {
    @Immutable
    data class State(
        val isLoading : Boolean = false,
        val isError : Boolean = false,
        val message : String = "",
        val data : List<RandomUser> = emptyList()
    )

    sealed interface Intent{
       data class Launch(val forced : Boolean) : Intent
       data class OnClickItem(val user: RandomUser) : Intent
    }


    sealed interface Message{
        data object Loading : Message
        data class Success(val data : List<RandomUser>) : Message
        data class Failure(val message: String) : Message
    }

    sealed interface Label{
        data class OnClickItem(val user: RandomUser) : Label
    }


}

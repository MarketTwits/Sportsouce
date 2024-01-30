package detail.presentation.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.random_user.RandomUser
import detail.presentation.store.UserDetailStore.Intent
import detail.presentation.store.UserDetailStore.Label
import detail.presentation.store.UserDetailStore.State

interface UserDetailStore : Store<Intent, State, Label> {
    @Immutable
    data class State(val item : RandomUser)

    sealed interface Intent{
        data object OnBackClicked : Intent
        data class OnClickEmail(val email : String) : Intent
        data class OnClickMap(val coordinates : String) : Intent
        data class OnClickGeoMap(val latitude : String) : Intent
        data class OnClickPhone(val phone : String) : Intent
    }

    sealed interface Message

    sealed interface Label{
        data object OnBackClicked : Label
    }

}

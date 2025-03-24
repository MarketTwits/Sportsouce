package com.markettwits.profile.internal.forgot_password.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Intent
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Label
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.State

interface ForgotPasswordStore : Store<Intent, State, Label> {
    data class State(
        val email: String = "",
        val isLoading: Boolean = false,
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Intent {
        data class OnValueChange(val email: String) : Intent
        data object OnClickReset : Intent
        data object OnConsumedEvent : Intent
        data object OnClickBack : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val message: String) : Message
        data class Success(val message: String) : Message
        data class OnValueChanged(val email: String) : Message
        data object OnConsumedEvent : Message
    }


    sealed interface Label {
        data object OnClickBack : Label
    }

}

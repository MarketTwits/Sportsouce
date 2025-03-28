package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore.Intent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore.Label
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore.State

interface SignUpStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnConsumedEvent : Intent
        data class ChangeValue(val statement: SignUpStatement) :Intent
        data object OnClickBack : Intent
        data object OnClickRegister : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val statement: SignUpStatement = SignUpStatement(),
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Label {
        data object OpenProfile : Label
        data object OnClickBack : Label
    }
}



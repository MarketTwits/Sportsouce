package com.markettwits.start.presentation.registration.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.domain.StartStatement

interface StartRegistrationStore : Store<StartRegistrationStore.Intent, StartRegistrationStore.State, StartRegistrationStore.Label> {

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val promoMessage : String = "",
        val startStatement: StartStatement? = null,
        val testEvent: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Label {
        data object GoBack : Label
    }
    sealed interface Intent {
        data object OnConsumedEvent : Intent
        data object OnClickSave : Intent
        data object OnClickPay : Intent
        data class ChangeFiled(val startStatement: StartStatement) : Intent
        data class ChangePromo(val value: String) : Intent
        data object GoBack : Intent
    }
}

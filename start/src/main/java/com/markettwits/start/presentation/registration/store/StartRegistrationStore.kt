package com.markettwits.start.presentation.registration.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.domain.StartStatement

interface StartRegistrationStore : Store<StartRegistrationStore.Intent, StartRegistrationStore.State, StartRegistrationStore.Label> {

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val startStatement: StartStatement? = null,
        val registrationSucceededEvent: StateEventWithContent<String> = consumed(),
        val registrationFailedEvent: StateEventWithContent<String> = consumed(),
    )

    sealed interface Label {
        data object GoBack : Label
    }
    sealed interface Intent {
        data object OnConsumedSucceededEvent : Intent
        data object OnConsumedFailedEvent : Intent
        data object OnClickSave : Intent
        data object OnClickPay : Intent
        data class ChangeFiled(val startStatement: StartStatement) : Intent
        data object GoBack : Intent
    }
}

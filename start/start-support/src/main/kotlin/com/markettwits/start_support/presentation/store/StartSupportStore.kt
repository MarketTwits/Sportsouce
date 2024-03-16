package com.markettwits.start_support.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.start_support.presentation.store.StartSupportStore.Intent
import com.markettwits.start_support.presentation.store.StartSupportStore.Label
import com.markettwits.start_support.presentation.store.StartSupportStore.State

interface StartSupportStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
        val cost: String = "",
        val eventWithContent: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Intent {
        data object OnConsumedEvent : Intent
        data class OnChangeValue(val value: String) : Intent
        data object OnClickSupport : Intent
    }

    sealed interface Message {
        data object OnConsumedEvent : Message
        data class OnValueChanged(val value: String) : Message
        data object Loading : Message
        data class Failure(val message: String) : Message
        data class Success(val url: String) : Message
    }

    sealed interface Label

}

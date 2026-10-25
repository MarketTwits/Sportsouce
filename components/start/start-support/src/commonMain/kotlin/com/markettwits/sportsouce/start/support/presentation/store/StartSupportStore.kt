package com.markettwits.sportsouce.start.support.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.*

interface StartSupportStore : Store<Intent, State, Label> {
    data class State(
        val startId: Int? = null,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
        val cost: String = "",
        val eventWithContent: StateEventWithContent<EventContent> = consumed(),
        val isOpen: Boolean = false,
        val checkState: CheckState = CheckState.Idle,
        val donationConfirmed: Boolean = false,
        val amountError: String? = null,
        val awaitingAuth: Boolean = false,
        val sheetSession: Int = 0,
    )

    sealed interface CheckState {
        data object Idle : CheckState
        data object Loading : CheckState
        data object Failed : CheckState
        data object AuthorizationRequired : CheckState
        data class Ready(val result: StartDonationCheck) : CheckState
    }

    sealed interface Intent {
        data class ApplyStartId(val startId: Int) : Intent
        data object OnConsumedEvent : Intent
        data class OnChangeValue(val value: String) : Intent
        data object OnClickSupport : Intent
        data object OnOpen : Intent
        data object OnClose : Intent
        data object OnRetryCheck : Intent
        data object OnConfirmDonation : Intent
        data object OnPayRegistration : Intent
        data object OnSignIn : Intent
        data object OnAuthReturned : Intent
    }

    sealed interface Message {
        data class UpdateStartId(val startId: Int) : Message
        data object OnConsumedEvent : Message
        data class OnValueChanged(val value: String) : Message
        data object Loading : Message
        data class Failure(val message: String) : Message
        data class Success(val url: String) : Message
        data object Open : Message
        data object Close : Message
        data object CheckLoading : Message
        data object CheckFailed : Message
        data class CheckReady(val result: StartDonationCheck) : Message
        data object ConfirmDonation : Message
        data class InvalidAmount(val message: String) : Message
        data object AuthorizationRequired : Message
        data object SuspendForAuth : Message
    }

    sealed interface Label {
        data object SignIn : Label
    }

}

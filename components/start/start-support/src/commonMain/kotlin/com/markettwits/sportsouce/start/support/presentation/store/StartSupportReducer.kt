package com.markettwits.sportsouce.start.support.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.Message
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.State
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.CheckState

internal object StartSupportReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failure -> copy(
                isError = true,
                isLoading = false,
                message = msg.message,
                eventWithContent = triggered(
                    EventContent(false, msg.message)
                )
            )

            is Message.Loading -> copy(isLoading = true, isError = false)
            is Message.OnValueChanged -> copy(cost = msg.value, amountError = null)
            is Message.Success -> copy(
                isSuccess = true,
                isLoading = false,
                message = msg.url,
                eventWithContent = triggered(
                    EventContent(true, msg.url)
                )
            )
            is Message.OnConsumedEvent -> copy(eventWithContent = consumed())
            is Message.UpdateStartId -> if (startId == msg.startId) this else State(startId = msg.startId, isOpen = isOpen)
            Message.Open -> copy(isOpen = true, checkState = CheckState.Loading, donationConfirmed = false,
                amountError = null, isError = false, isSuccess = false, message = "", eventWithContent = consumed(),
                awaitingAuth = false, sheetSession = sheetSession + 1)
            Message.Close -> copy(isOpen = false, isLoading = false, checkState = CheckState.Idle,
                donationConfirmed = false, eventWithContent = consumed(), awaitingAuth = false)
            Message.CheckLoading -> copy(checkState = CheckState.Loading, donationConfirmed = false)
            Message.CheckFailed -> copy(checkState = CheckState.Failed)
            is Message.CheckReady -> copy(checkState = CheckState.Ready(msg.result))
            Message.ConfirmDonation -> copy(donationConfirmed = true)
            is Message.InvalidAmount -> copy(amountError = msg.message)
            Message.AuthorizationRequired -> copy(checkState = CheckState.AuthorizationRequired,
                isLoading = false, isError = false, donationConfirmed = false, amountError = null,
                eventWithContent = consumed())
            Message.SuspendForAuth -> copy(isOpen = false, awaitingAuth = true, isLoading = false,
                checkState = CheckState.Idle, donationConfirmed = false, eventWithContent = consumed())
        }
    }
}

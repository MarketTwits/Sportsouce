package com.markettwits.registrations.paymant_dialog

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.registrations.paymant_dialog.components.RegistrationsPaymentCard
import com.markettwits.registrations.registrations.presentation.components.RegistrationsStart

@Composable
fun RegistrationsPaymentScreen(component : RegistrationsPaymentComponent) {
    val state = component.state.subscribeAsState()
    RegistrationsPaymentCard(state.value)
}
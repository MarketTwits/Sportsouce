package com.markettwits.registrations.paymant_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.markettwits.registrations.paymant_dialog.components.RegistrationsPaymentCard
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore

@Composable
fun RegistrationsPaymentScreen(component : RegistrationsPaymentComponent) {
    val state = component.state.collectAsState()
    RegistrationsPaymentCard(state.value){
        component.obtainEvent(RegistrationsPaymentStore.Intent.OnClickPay(it))
    }
}
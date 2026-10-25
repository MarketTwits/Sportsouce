package com.markettwits.sportsouce.start.support.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore

@Composable
fun StartSupportScreen(
    modifier: Modifier = Modifier,
    component: StartSupportComponent,
    onClickDismiss: () -> Unit,
    event: (EventContent) -> Unit
) {
    val state by component.state.collectAsState()
    StartSupportPanel(
        modifier = modifier,
        state = state,
        onRetryCheck = { component.obtainEvent(StartSupportStore.Intent.OnRetryCheck) },
        onConfirmDonation = { component.obtainEvent(StartSupportStore.Intent.OnConfirmDonation) },
        onPayRegistration = { component.obtainEvent(StartSupportStore.Intent.OnPayRegistration) },
        onSignIn = { component.obtainEvent(StartSupportStore.Intent.OnSignIn) },
        onValueChanged = {
            component.obtainEvent(StartSupportStore.Intent.OnChangeValue(it))
        }, onClickSupport = {
            component.obtainEvent(StartSupportStore.Intent.OnClickSupport)
        }
    )
    EventEffect(event = state.eventWithContent, onConsumed = {
        component.obtainEvent(StartSupportStore.Intent.OnConsumedEvent)
    }) {
        if (it.success) onClickDismiss()
        else
            event(EventContent(false, state.message))
    }
}

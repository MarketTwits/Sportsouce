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
        value = state.cost,
        isEnabled = !state.isLoading && state.cost.isNotEmpty(),
        onValueChanged = {
            component.obtainEvent(StartSupportStore.Intent.OnChangeValue(it))
        }, onClickSupport = {
            component.obtainEvent(StartSupportStore.Intent.OnClickSupport)
            onClickDismiss()
        }
    )
    EventEffect(event = state.eventWithContent, onConsumed = {
        component.obtainEvent(StartSupportStore.Intent.OnConsumedEvent)
    }) {
        if (!it.success)
            event(EventContent(false, state.message))
    }
}
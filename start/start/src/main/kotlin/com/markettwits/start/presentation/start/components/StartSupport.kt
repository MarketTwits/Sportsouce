package com.markettwits.start.presentation.start.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.event.EventContent
import com.markettwits.start.presentation.common.StartContentBasePanel
import com.markettwits.start_support.presentation.component.StartSupportComponent
import com.markettwits.start_support.presentation.components.StartSupportScreen

@Composable
fun StartSupport(
    modifier: Modifier,
    component: StartSupportComponent,
    eventContent: (EventContent) -> Unit
) {
    StartContentBasePanel(modifier = modifier, label = "Поддержкать проект") {
        StartSupportScreen(modifier = modifier, component = component, event = eventContent)
    }
}
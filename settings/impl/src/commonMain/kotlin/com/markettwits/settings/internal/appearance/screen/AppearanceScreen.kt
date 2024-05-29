package com.markettwits.settings.internal.appearance.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.settings.internal.appearance.component.AppearanceComponent
import com.markettwits.settings.internal.appearance.components.AppearanceContent
import com.markettwits.settings.internal.appearance.store.AppearanceStore

@Composable
fun AppearanceScreen(component: AppearanceComponent) {
    val state by component.state.collectAsState()
    AppearanceContent(
        state = state,
        onClickGoBack = {
            component.obtainEvent(AppearanceStore.Intent.OnClickGoBack)
        },
        onClickItem = {
            component.obtainEvent(AppearanceStore.Intent.OnClickItem(it))
        }
    )
}
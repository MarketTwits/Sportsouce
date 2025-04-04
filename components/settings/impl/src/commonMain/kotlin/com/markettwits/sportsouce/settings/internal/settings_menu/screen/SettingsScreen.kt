package com.markettwits.sportsouce.settings.internal.settings_menu.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsComponent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.SettingsContent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultApplicationElements
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultSocialElements
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val isSystemInDarkTheme = LocalDarkOrLightTheme.current
    val applicationItems = defaultApplicationElements(isSystemInDarkTheme)
    val state by component.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithClip(title = "Настройки") {
                component.obtainEvent(SettingsStore.Intent.GoBack)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        SettingsContent(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            applicationsMenu = applicationItems,
            socialMenu = defaultSocialElements(),
            versionName = state.version?.versionName ?: "",
            versionBuildNumber = state.version?.versionBuildNumber ?: "",
            onClickMenu = {
                component.obtainEvent(SettingsStore.Intent.OnClickItemMenu(it.id))
            }
        )
    }
}
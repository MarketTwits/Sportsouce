package com.markettwits.sportsouce.settings.internal.settings_menu.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.dialog.SauceDialog
import com.markettwits.core_ui.items.components.dialog.SauceDialogContent
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsComponent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.SettingsContent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultApplicationElements
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultSocialElements
import com.markettwits.sportsouce.settings.internal.settings_menu.components.notificationSettingsMenuItem
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val isSystemInDarkTheme = LocalDarkOrLightTheme.current
    val state by component.state.collectAsState()
    val applicationItems = buildList {
        addAll(defaultApplicationElements(isSystemInDarkTheme, state.cacheSize))
        if (state.showNotificationSettings) add(notificationSettingsMenuItem)
    }

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
                component.obtainEvent(
                    if (it == notificationSettingsMenuItem) SettingsStore.Intent.OpenNotificationSettings
                    else SettingsStore.Intent.OnClickItemMenu(it.id),
                )
            }
        )
    }

    if (state.showClearCacheDialog) {
        val dismiss = { component.obtainEvent(SettingsStore.Intent.HideClearCacheDialog) }
        SauceDialog(onDismissRequest = dismiss) {
            SauceDialogContent(
                title = "Очистить кеш?",
                actions = {
                    SauceButton(
                        "Очистить",
                        onClick = { component.obtainEvent(SettingsStore.Intent.ClearCache) },
                        variant = SauceButtonVariant.Destructive,
                    )
                    SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Text)
                },
            ) {
                Text(
                    text = "Будут удалены временные файлы приложения. Размер кеша: ${state.cacheSize}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SauceTheme.colors.textSecondary,
                )
            }
        }
    }
}

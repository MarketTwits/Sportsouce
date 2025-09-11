package com.markettwits.sportsouce.settings.internal.settings_menu.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsComponent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.SettingsContent
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultApplicationElements
import com.markettwits.sportsouce.settings.internal.settings_menu.components.defaultSocialElements
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val isSystemInDarkTheme = LocalDarkOrLightTheme.current
    val state by component.state.collectAsState()
    val applicationItems = defaultApplicationElements(isSystemInDarkTheme, state.cacheSize)

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

    if (state.showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = {
                component.obtainEvent(SettingsStore.Intent.HideClearCacheDialog)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            title = {
                Text(
                    text = "Очистить кеш",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Вы действительно хотите очистить кеш приложения? Размер кеша: ${state.cacheSize}",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.medium(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                ButtonContentBase(
                    containerColor = SportSouceColor.SportSouceLightRed,
                    textColor = Color.White,
                    title = "Очистить",
                    onClick = {
                        component.obtainEvent(SettingsStore.Intent.ClearCache)
                    }
                )
            },
            dismissButton = {
                ButtonContentBase(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    title = "Отмена",
                    onClick = {
                        component.obtainEvent(SettingsStore.Intent.HideClearCacheDialog)
                    }
                )
            }
        )
    }
}
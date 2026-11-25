package com.markettwits.sportsouce.settings.internal.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences.State
import com.markettwits.sportsouce.notification.api.model.PushPermissionState
import com.markettwits.sportsouce.settings.internal.notifications.NotificationSettingsStore.Intent

@Composable
internal fun NotificationSettingsScreen(component: NotificationSettingsComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Уведомления") { component.accept(Intent.Back) } }) { padding ->
        AdaptivePane(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            Column(
                modifier = Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState()).padding(SauceSpacing.medium),
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
            ) {
                OnBackgroundCard(Modifier.fillMaxWidth()) {
                    SauceSwitchRow(
                        label = "Пуши на этом устройстве",
                        checked = state.supported && state.enabled &&
                            state.permission == PushPermissionState.Granted && state.channelEnabled,
                        onCheckedChange = { component.accept(Intent.SetEnabled(it)) },
                        supportingText = if (state.supported) "О стартах, регистрациях и профиле. Сообщения всегда доступны в ленте."
                        else "Пуши недоступны на этом устройстве.",
                        enabled = state.supported,
                    )
                }
                if (state.supported) {
                    AndroidPermissionCard(state.permission, component::accept)
                    NotificationChannelCard(state, component::accept)
                }
            }
        }
    }
}

@Composable
private fun AndroidPermissionCard(permission: PushPermissionState, accept: (Intent) -> Unit) {
    val granted = permission == PushPermissionState.Granted
    val needsSettings = permission == PushPermissionState.Settings
    OnBackgroundCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(SauceSpacing.medium), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Text("Разрешение на уведомления", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            SauceStatusBadge(
                label = if (granted) "Разрешено" else "Выключено",
                tone = if (granted) SauceStatusTone.Success else SauceStatusTone.Warning,
            )
            if (!granted) {
                Text("Разрешите Sportsauce показывать уведомления.",
                    style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            }
            if (permission in listOf(PushPermissionState.Request, PushPermissionState.Explain, PushPermissionState.RetryOrSettings)) {
                SauceButton("Запросить разрешение", { accept(Intent.RequestPermission) }, variant = SauceButtonVariant.Soft)
            }
            SauceButton("Системные настройки", { accept(Intent.OpenAppSettings) },
                variant = if (needsSettings) SauceButtonVariant.Soft else SauceButtonVariant.Outlined)
        }
    }
}

@Composable
private fun NotificationChannelCard(state: State, accept: (Intent) -> Unit) {
    val granted = state.permission == PushPermissionState.Granted
    OnBackgroundCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(SauceSpacing.medium), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Text("Звук и показ", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            if (granted && !state.channelEnabled) {
                SauceStatusBadge("Канал отключён", tone = SauceStatusTone.Warning)
            }
            Text("Звук, вибрацию и показ на экране блокировки можно изменить в настройках канала.",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            SauceButton("Настроить канал уведомлений", { accept(Intent.OpenChannelSettings) },
                variant = SauceButtonVariant.Outlined, enabled = granted)
        }
    }
}

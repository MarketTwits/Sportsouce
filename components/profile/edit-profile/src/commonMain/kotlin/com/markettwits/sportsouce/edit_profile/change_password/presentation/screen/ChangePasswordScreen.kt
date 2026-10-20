package com.markettwits.sportsouce.edit_profile.change_password.presentation.screen

import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.edit_profile.change_password.presentation.component.ChangePasswordTextField
import com.markettwits.sportsouce.edit_profile.change_password.presentation.component.SaveChangesButton

@Composable
fun ChangePasswordScreen(component: ChangePassword) {
    val state by component.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var feedbackSuccess by remember { mutableStateOf(true) }
    val feedbackColors = if (feedbackSuccess) SauceTheme.colors.success else SauceTheme.colors.danger
    val focusManager = LocalFocusManager.current
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbar) {
                Snackbar(it, contentColor = feedbackColors.content, containerColor = feedbackColors.container,
                    dismissActionContentColor = feedbackColors.content)
            }
        },
        topBar = { TopBarWithClip(title = "Сменить пароль", goBack = component::back) },
        bottomBar = {
            AdaptivePane(fillHeight = false) {
                SaveChangesButton(Modifier.fillMaxWidth().imePadding().padding(horizontal = 16.dp, vertical = 12.dp),
                    loading = state.isLoading) {
                    focusManager.clearFocus()
                    component.onSaveChanged()
                }
            }
        }
    ) { padding ->
        AdaptivePane {
            Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp)) {
                OnBackgroundCard {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChangePasswordTextField(value = state.currentPassword,
                            onValueChange = component::onCurrentPasswordChanged, label = "Старый пароль")
                        ChangePasswordTextField(value = state.newPassword,
                            onValueChange = component::onNewPasswordChanged, label = "Новый пароль")
                        ChangePasswordTextField(value = state.newRepeatPassword,
                            onValueChange = component::onNewPasswordRepeatChanged, label = "Повторите новый пароль")
                    }
                }
            }
        }
    }
    EventEffect(state.downloadFailedEvent, component::onConsumedDownloadFailedEvent) {
        feedbackSuccess = false
        snackbar.showLongMessageWithDismiss(message = it)
    }
    EventEffect(state.downloadSucceededEvent, component::onConsumedDownloadSucceededEvent) {
        feedbackSuccess = true
        snackbar.showLongMessageWithDismiss(message = it)
    }
}

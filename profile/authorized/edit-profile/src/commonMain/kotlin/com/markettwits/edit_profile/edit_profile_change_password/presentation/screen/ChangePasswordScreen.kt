
package com.markettwits.edit_profile.edit_profile_change_password.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.event.isTriggered
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.edit_profile.edit_profile_change_password.presentation.component.ChangePasswordTextField
import com.markettwits.edit_profile.edit_profile_change_password.presentation.component.SaveChangesButton


@Composable
fun ChangePasswordScreen(component: ChangePassword) {
    val state by component.state.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = if (state.downloadSucceededEvent.isTriggered()) SportSouceColor.SportSouceLighBlue
                    else SportSouceColor.SportSouceLightRed,
                    snackbarData = it
                )
            }
        },
        topBar = {
            TopBarWithClip(title = "Сменить пароль") {
                component.back()
            }
        }
    ) {
        OnBackgroundCard(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(20.dp)
        ) { innerModifier ->
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .clip(Shapes.medium)
            ) {
                val modifier = Modifier.padding(5.dp)
                ChangePasswordTextField(
                    modifier = modifier,
                    value = state.currentPassword,
                    onValueChange = { newValue -> component.onCurrentPasswordChanged(newValue) },
                    label = "Старый пароль"
                )
                ChangePasswordTextField(
                    modifier = modifier,
                    value = state.newPassword,
                    onValueChange = { newValue -> component.onNewPasswordChanged(newValue) },
                    label = "Новый пароль"
                )
                ChangePasswordTextField(
                    modifier = modifier,
                    value = state.newRepeatPassword,
                    onValueChange = { newValue -> component.onNewPasswordRepeatChanged(newValue) },
                    label = "Повторите новый пароль"
                )
                SaveChangesButton(modifier = modifier, loading = state.isLoading) {
                    component.onSaveChanged()
                }

                EventEffect(
                    event = state.downloadFailedEvent,
                    onConsumed = component::onConsumedDownloadFailedEvent,
                ) {
                    snackBarHostState.showLongMessageWithDismiss(message = it)
                }
                EventEffect(
                    event = state.downloadSucceededEvent,
                    onConsumed = component::onConsumedDownloadSucceededEvent,
                ) {
                    snackBarHostState.showLongMessageWithDismiss(message = it)
                }
            }
        }
    }
}
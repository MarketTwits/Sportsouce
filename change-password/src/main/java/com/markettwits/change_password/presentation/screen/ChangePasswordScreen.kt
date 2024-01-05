@file:JvmName("StartScreenKt")

package com.markettwits.change_password.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.change_password.presentation.component.ChangePasswordTextField
import com.markettwits.change_password.presentation.component.MyProfileTopBar
import com.markettwits.change_password.presentation.component.SaveChangesButton
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.event.isTriggered
import com.markettwits.core_ui.theme.SportSouceColor


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
            MyProfileTopBar(title = "Сменить пароль") {
                component.back()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
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
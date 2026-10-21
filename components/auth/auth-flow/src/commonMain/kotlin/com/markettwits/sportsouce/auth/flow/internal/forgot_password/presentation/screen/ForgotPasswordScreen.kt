package com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.component.ForgotPasswordComponent
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.components.ForgotPasswordContent
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.store.ForgotPasswordStore

@Composable
internal fun ForgotPasswordScreen(component: ForgotPasswordComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarTone by remember { mutableStateOf(SauceSnackbarTone.Neutral) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SauceSnackbarHost(
                hostState = snackBarHostState,
                tone = snackBarTone,
            )
        }
    ) {
        ForgotPasswordContent(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(10.dp),
            email = state.email,
            isLoading = state.isLoading,
            onClickRecover = {
                component.obtainEvent(ForgotPasswordStore.Intent.OnClickReset)
            },
            onValueChanged = {
                component.obtainEvent(ForgotPasswordStore.Intent.OnValueChange(it))
            },
            onClickConsume = {
                component.obtainEvent(ForgotPasswordStore.Intent.OnClickBack)
            }
        )
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(ForgotPasswordStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarTone = if (it.success) SauceSnackbarTone.Success else SauceSnackbarTone.Error
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }


}
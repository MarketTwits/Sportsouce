package com.markettwits.profile.internal.forgot_password.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.profile.internal.forgot_password.presentation.component.ForgotPasswordComponent
import com.markettwits.profile.internal.forgot_password.presentation.components.ForgotPasswordContent
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore

@Composable
internal fun ForgotPasswordScreen(component: ForgotPasswordComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarColor = remember {
        SportSouceColor.SportSouceLighBlue
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    dismissActionContentColor = Color.White,
                    snackbarData = it
                )
            }
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
            snackBarColor =
                if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }


}
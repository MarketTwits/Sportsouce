package com.markettwits.profile.internal.sign_in.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.internal.common.AuthButton
import com.markettwits.profile.internal.common.WelcomeContent
import com.markettwits.profile.internal.sign_in.presentation.component.SignInFieldUiState
import com.markettwits.profile.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.profile.internal.sign_in.presentation.component.SignInUiState
import kotlinx.coroutines.launch

@Composable
internal fun SignInContent(state: SignInUiState, fieldState: SignInFieldUiState, component: SignInScreen) {
    val scope = rememberCoroutineScope()
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeContent()
            val modifier = Modifier.padding(10.dp)
            OutlinePhoneTextFiled(
                modifier = modifier,
                label = "Номер телефона",
                value = fieldState.email,
                isError = state is SignInUiState.Error,
            ) {
                component.handlePhone(it)
            }
            OutlinedTextFieldBase(
                modifier = modifier,
                label = "Пароль",
                value = fieldState.password,
                visualTransformation = PasswordVisualTransformation(),
                isError = state is SignInUiState.Error
            ) {
                component.handlePassword(it)
            }
            ForgotPasswordLabel(modifier = modifier) {
                component.forgotPassword()
            }
            AuthButton(
                modifier = modifier,
                "Войти",
                enabled = state != SignInUiState.Loading && fieldState.enabled,
                loading = state is SignInUiState.Loading
            ) {
                component.logIn()
            }
            SignUpLabel(modifier = modifier.padding(top = 10.dp)) {
                component.signUp()
            }

        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = SportSouceColor.SportSouceLightRed,
                snackbarData = it
            )
        }
        if ((state is SignInUiState.Error)) {
            if (!state.messageShow) {
                LaunchedEffect(snackbarHostState) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = state.message,
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                        component.messageHasBeenShowed()
                    }
                }
            }
        }
    }
}
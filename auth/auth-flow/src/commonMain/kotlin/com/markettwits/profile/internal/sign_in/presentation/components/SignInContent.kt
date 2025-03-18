package com.markettwits.profile.internal.sign_in.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.profile.internal.common.OutlinePasswordTextField
import com.markettwits.profile.internal.common.WelcomeContent
import com.markettwits.profile.internal.sign_in.presentation.component.SignInFieldUiState
import com.markettwits.profile.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.profile.internal.sign_in.presentation.component.SignInUiState
import kotlinx.coroutines.launch

@Composable
internal fun SignInContent(
    state: SignInUiState,
    fieldState: SignInFieldUiState,
    component: SignInScreen
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val focusManager = LocalFocusManager.current
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
            OutlinePasswordTextField(
                modifier = modifier,
                label = "Пароль",
                value = fieldState.password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state is SignInUiState.Error
            ) {
                component.handlePassword(it)
            }
            UnderFieldsContent(
                isButtonEnabled = state != SignInUiState.Loading && fieldState.enabled,
                isButtonLoading = state is SignInUiState.Loading,
                onClickRegistry = {
                    component.forgotPassword()
                },
                onClickAuth = {
                    component.logIn()
                    focusManager.clearFocus()
                }
            )
            CreateProfileAndBackContent(
                onClickRegistry = {
                    component.signUp()
                },
                onClickConsume = {
                    component.back()
                }
            )
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
                scope.launch {
                    snackbarHostState.showLongMessageWithDismiss(state.message)
                    component.messageHasBeenShowed()
                }
            }
        }
    }
}
package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import SignInLoginMethodToggle
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.event.isTriggered
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.auth.flow.internal.common.*
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore
import kotlinx.coroutines.launch

@Composable
internal fun SignInContent(
    state: SignInStore.State,
    component: SignInScreen,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val focusManager = LocalFocusManager.current

    // Focus requesters for field navigation
    val passwordFocusRequester = remember { FocusRequester() }
    val smsCodeFocusRequester = remember { FocusRequester() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) { snackbarData ->
                Snackbar(
                    contentColor = Color.White,
                    containerColor = SportSouceColor.SportSouceLightRed,
                    snackbarData = snackbarData
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            AuthAlreadySomeActionBox(
                onClick = {
                    component.obtainEvent(SignInStore.Intent.SignUp)
                    focusManager.clearFocus()
                },
                actionText = "Зарегистрироваться",
                descriptionText = "Ещё нет аккаунт ?"
            )
        },
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthWelcomeContent()

                // Login method toggle
                SignInLoginMethodToggle(
                    currentLoginMethod = state.loginMethod,
                    onLoginMethodChange = { component.obtainEvent(SignInStore.Intent.SetLoginMethod(it)) },
                )

                Spacer(Modifier.height(10.dp))

                // Email or Phone field (always visible)
                EnhancedEmailOrPhoneTextField(
                    label = if (state.loginMethod == LoginMethod.SMS) "Номер телефона" else "Телефон или почта",
                    value = state.emailOrPhone,
                    isError = state.emailOrPhoneError != null,
                    errorMessage = state.emailOrPhoneError,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            when (state.loginMethod) {
                                LoginMethod.PASSWORD -> passwordFocusRequester.requestFocus()
                                LoginMethod.SMS -> smsCodeFocusRequester.requestFocus()
                            }
                        }
                    ),
                    onValueChanged = { component.obtainEvent(SignInStore.Intent.UpdateEmailOrPhone(it)) },
                    onFocusChanged = { component.obtainEvent(SignInStore.Intent.SetEmailOrPhoneFocus(it)) }
                )

                Spacer(Modifier.height(10.dp))

                // Conditional fields based on login method with smooth transitions
                AnimatedVisibility(
                    visible = state.loginMethod == LoginMethod.PASSWORD,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 4 })
                ) {
                    EnhancedPasswordTextField(
                        modifier = Modifier.focusRequester(passwordFocusRequester),
                        label = "Пароль",
                        value = state.password,
                        isError = state.passwordError != null,
                        errorMessage = state.passwordError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                component.obtainEvent(SignInStore.Intent.Login)
                                focusManager.clearFocus()
                            }
                        ),
                        onValueChanged = { component.obtainEvent(SignInStore.Intent.UpdatePassword(it)) },
                        onFocusChanged = { component.obtainEvent(SignInStore.Intent.SetPasswordFocus(it)) }
                    )
                }

                AnimatedVisibility(
                    visible = state.loginMethod == LoginMethod.SMS,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 4 })
                ) {
                    EnhancedSmsCodeTextField(
                        modifier = Modifier.focusRequester(smsCodeFocusRequester),
                        label = "СМС код",
                        value = state.smsCode,
                        isError = state.smsCodeError != null,
                        errorMessage = state.smsCodeError,
                        isSmsCodeSent = state.isSmsCodeSent,
                        smsCodeSending = state.smsCodeSending,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                component.obtainEvent(SignInStore.Intent.Login)
                                focusManager.clearFocus()
                            }
                        ),
                        onValueChanged = { component.obtainEvent(SignInStore.Intent.UpdateSmsCode(it)) },
                        onFocusChanged = { component.obtainEvent(SignInStore.Intent.SetSmsCodeFocus(it)) },
                        onSendSmsClick = { component.obtainEvent(SignInStore.Intent.SendSmsCode) }
                    )
                }
                SignInUnderFieldsContent(
                    isButtonEnabled = !state.isLoading && state.enabled,
                    isButtonLoading = state.isLoading,
                    onClickRegistry = {
                        component.forgotPassword()
                    },
                    onClickAuth = {
                        component.obtainEvent(SignInStore.Intent.Login)
                        focusManager.clearFocus()
                    }
                )
                CreateProfileAndBackContent(
                    onClickRegistry = {
                        component.obtainEvent(SignInStore.Intent.SignUp)
                    },
                    onClickConsume = {
                        component.back()
                    }
                )
            }
            if (state.event.isTriggered()) {
                scope.launch {
                    snackbarHostState.showLongMessageWithDismiss((state.event as com.markettwits.core_ui.items.event.StateEventWithContentTriggered).content.message)
                    component.obtainEvent(SignInStore.Intent.MessageHasBeenShowed)
                }
            }
            BackFloatingActionButton(back = {
                component.back()
                focusManager.clearFocus()
            })
        }
    }
}
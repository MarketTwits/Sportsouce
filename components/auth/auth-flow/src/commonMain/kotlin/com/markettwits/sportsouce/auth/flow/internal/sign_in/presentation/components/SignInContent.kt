package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.auth.flow.internal.common.*
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore

@Composable
internal fun SignInContent(
    state: SignInStore.State,
    component: SignInScreen,
) {
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val focusManager = LocalFocusManager.current

    // Focus requesters for field navigation
    val passwordFocusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SauceSnackbarHost(
                hostState = snackbarHostState,
                tone = SauceSnackbarTone.Error,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthWelcomeContent()

                // Login method toggle
                SignInLoginMethodToggle(
                    currentLoginMethod = state.loginMethod,
                    onLoginMethodChange = {
                        focusManager.clearFocus()
                        component.obtainEvent(SignInStore.Intent.SetLoginMethod(it))
                    },
                    enabled = !state.isLoading,
                )

                Spacer(Modifier.height(10.dp))

                AnimatedContent(
                    targetState = state.loginMethod,
                    transitionSpec = {
                        if (targetState == LoginMethod.VK) {
                            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                        } else {
                            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                        }
                    },
                    label = "Login method",
                ) { method ->
                    when (method) {
                        LoginMethod.VK -> VkSignInUnavailableContent()
                        LoginMethod.PASSWORD -> Column {
                            EnhancedEmailOrPhoneTextField(
                                label = "Телефон или почта",
                                value = state.emailOrPhone,
                                isError = state.emailOrPhoneError != null,
                                errorMessage = state.emailOrPhoneError,
                                hintMessage = "Номер телефона +79136661217 либо электронная почта",
                                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                                onValueChanged = { component.obtainEvent(SignInStore.Intent.UpdateEmailOrPhone(it)) },
                                onFocusChanged = { component.obtainEvent(SignInStore.Intent.SetEmailOrPhoneFocus(it)) },
                            )
                            Spacer(Modifier.height(10.dp))
                            EnhancedPasswordTextField(
                                modifier = Modifier.focusRequester(passwordFocusRequester),
                                label = "Пароль",
                                value = state.password,
                                isError = state.passwordError != null,
                                errorMessage = state.passwordError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                keyboardActions = KeyboardActions(onDone = {
                                    component.obtainEvent(SignInStore.Intent.Login)
                                    focusManager.clearFocus()
                                }),
                                onValueChanged = { component.obtainEvent(SignInStore.Intent.UpdatePassword(it)) },
                                onFocusChanged = { component.obtainEvent(SignInStore.Intent.SetPasswordFocus(it)) },
                            )
                            SignInUnderFieldsContent(
                                isButtonEnabled = !state.isLoading && state.enabled,
                                isButtonLoading = state.isLoading,
                                onClickRegistry = component::forgotPassword,
                                onClickAuth = {
                                    component.obtainEvent(SignInStore.Intent.Login)
                                    focusManager.clearFocus()
                                },
                            )
                            CreateProfileAndBackContent(
                                onClickRegistry = { component.obtainEvent(SignInStore.Intent.SignUp) },
                                onClickConsume = component::back,
                            )
                        }
                    }
                }
            }
            EventEffect(
                event = state.event,
                onConsumed = { component.obtainEvent(SignInStore.Intent.MessageHasBeenShowed) },
            ) { event ->
                snackbarHostState.showLongMessageWithDismiss(event.message)
            }

            BackFloatingActionButton(back = {
                component.back()
                focusManager.clearFocus()
            })
        }
    }
}
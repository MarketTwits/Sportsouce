package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.InputType
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.ValidationUtils
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore.*
import kotlinx.coroutines.launch

class SignInExecutor(
    private val useCase: SignInUseCase,
    private val exceptionTracker: ExceptionTracker,
) : CoroutineExecutor<Intent, Unit, State, SignInReducer.Message, Label>() {

    /**
     * Helper method to detect input type internally without storing in state
     */
    private fun detectInputType(input: String): InputType {
        return ValidationUtils.detectInputType(input)
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Login -> login()
            is Intent.SendSmsCode -> sendSmsCode()
            is Intent.UpdateEmailOrPhone -> updateEmailOrPhone(intent.input)
            is Intent.UpdatePassword -> updatePassword(intent.password)
            is Intent.UpdateSmsCode -> updateSmsCode(intent.smsCode)
            is Intent.SetLoginMethod -> setLoginMethod(intent.method)
            is Intent.SetEmailOrPhoneFocus -> setEmailOrPhoneFocus(intent.focused)
            is Intent.SetPasswordFocus -> setPasswordFocus(intent.focused)
            is Intent.SetSmsCodeFocus -> setSmsCodeFocus(intent.focused)
            is Intent.SignUp -> publish(Label.GoSignUp)
            is Intent.MessageHasBeenShowed -> dispatch(SignInReducer.Message.MessageConsumed)
        }
    }

    private fun login() {
        val currentState = state()

        when (currentState.loginMethod) {
            LoginMethod.PASSWORD -> loginWithPassword()
            LoginMethod.SMS -> {
                if (currentState.isSmsCodeSent) {
                    loginWithSmsCode()
                } else {
                    sendSmsCode()
                }
            }
        }
    }

    private fun loginWithPassword() {
        // Validate all fields before login
        validateAllFields()

        val currentState = state()
        if (!currentState.enabled) return

        scope.launch {
            dispatch(SignInReducer.Message.Loading)

            val inputType = detectInputType(currentState.emailOrPhone)
            val emailOrPhone = when (inputType) {
                InputType.PHONE -> ValidationUtils.formatPhoneForApi(currentState.emailOrPhone)
                else -> currentState.emailOrPhone
            }

            useCase.signIn(emailOrPhone, currentState.password).fold(
                onSuccess = {
                    dispatch(SignInReducer.Message.Success)
                    exceptionTracker.setUserId(it.id.toString())
                    publish(Label.GoProfile)
                }, onFailure = {
                    val message = it.networkExceptionHandler().message.toString()
                    exceptionTracker.setLog("emailOrPhone: ${currentState.emailOrPhone}\npassword: ${currentState.password}")
                    exceptionTracker.reportException(it, key = "#SignInExecutor#loginWithPassword")
                    dispatch(SignInReducer.Message.Error(message))
                })
        }
    }

    private fun loginWithSmsCode() {
        // Validate all fields before login
        validateAllFields()

        val currentState = state()
        if (!currentState.enabled) return

        scope.launch {
            dispatch(SignInReducer.Message.Loading)

            val phoneNumber = ValidationUtils.formatPhoneForApi(currentState.emailOrPhone)
            try {
                // TODO: Implement actual SMS code verification API call
                // For now, simulate SMS verification
                kotlinx.coroutines.delay(1000)

                // Simulate successful SMS verification
                if (currentState.smsCode.length >= 4) {
                    dispatch(SignInReducer.Message.Success)
                    publish(Label.GoProfile)
                } else {
                    dispatch(SignInReducer.Message.Error("Неверный код из СМС"))
                }
            } catch (e: Exception) {
                exceptionTracker.setLog("phone: $phoneNumber\nsmsCode: ${currentState.smsCode}")
                exceptionTracker.reportException(e, key = "#SignInExecutor#loginWithSmsCode")
                dispatch(SignInReducer.Message.Error("Ошибка проверки СМС кода"))
            }
        }
    }

    private fun updateEmailOrPhone(input: String) {
        dispatch(SignInReducer.Message.UpdateEmailOrPhone(input))

        // Validate in real-time as user types
        if (input.isNotBlank()) {
            val inputType = detectInputType(input)
            val validation = ValidationUtils.validateInput(input, inputType)
            dispatch(
                SignInReducer.Message.SetFieldError(
                    emailOrPhoneError = validation.errorMessage,
                    passwordError = state().passwordError,
                    smsCodeError = state().smsCodeError
                )
            )
        } else {
            // Clear error when field is empty
            dispatch(
                SignInReducer.Message.SetFieldError(
                    emailOrPhoneError = null,
                    passwordError = state().passwordError,
                    smsCodeError = state().smsCodeError
                )
            )
        }

        updateButtonState()
    }

    private fun updatePassword(password: String) {
        dispatch(SignInReducer.Message.UpdatePassword(password))

        // Don't validate in real-time, only update button state
        updateButtonState()
    }

    private fun updateSmsCode(smsCode: String) {
        dispatch(SignInReducer.Message.UpdateSmsCode(smsCode))

        // Don't validate in real-time, only update button state
        updateButtonState()
    }

    private fun setLoginMethod(method: LoginMethod) {
        dispatch(SignInReducer.Message.SetLoginMethod(method))
        updateButtonState()
    }

    private fun setEmailOrPhoneFocus(focused: Boolean) {
        dispatch(SignInReducer.Message.SetEmailOrPhoneFocus(focused))
        // Don't validate on focus change, only update button state
        if (!focused) {
            updateButtonState()
        }
    }

    private fun setPasswordFocus(focused: Boolean) {
        dispatch(SignInReducer.Message.SetPasswordFocus(focused))
        // Don't validate on focus change, only update button state
        if (!focused) {
            updateButtonState()
        }
    }

    private fun setSmsCodeFocus(focused: Boolean) {
        dispatch(SignInReducer.Message.SetSmsCodeFocus(focused))
        // Don't validate on focus change, only update button state
        if (!focused) {
            updateButtonState()
        }
    }

    private fun sendSmsCode() {
        // Validate phone number first
        validateEmailOrPhone()

        val currentState = state()
        val inputType = detectInputType(currentState.emailOrPhone)
        if (inputType != InputType.PHONE || currentState.emailOrPhoneError != null) {
            return
        }

        scope.launch {
            dispatch(SignInReducer.Message.SmsCodeSendingStarted)

            try {
                ValidationUtils.formatPhoneForApi(currentState.emailOrPhone)
                // TODO: Implement actual SMS sending API call
                // For now, simulate SMS sending
                kotlinx.coroutines.delay(2000)

                dispatch(SignInReducer.Message.SmsCodeSent)
                dispatch(SignInReducer.Message.Error("Авторизация через СМС временно недоступна"))
            } catch (e: Exception) {
                dispatch(SignInReducer.Message.SmsCodeSendingFailed)
                dispatch(SignInReducer.Message.Error("Ошибка отправки СМС"))
            }
        }
    }

    private fun validateEmailOrPhone() {
        val currentState = state()
        val inputType = detectInputType(currentState.emailOrPhone)
        val validation = ValidationUtils.validateInput(currentState.emailOrPhone, inputType)

        dispatch(
            SignInReducer.Message.SetFieldError(
                emailOrPhoneError = validation.errorMessage,
                passwordError = currentState.passwordError,
                smsCodeError = currentState.smsCodeError
            )
        )
    }

    private fun validatePassword() {
        val currentState = state()
        val validation = ValidationUtils.validatePassword(currentState.password)

        dispatch(
            SignInReducer.Message.SetFieldError(
                emailOrPhoneError = currentState.emailOrPhoneError,
                passwordError = validation.errorMessage,
                smsCodeError = currentState.smsCodeError
            )
        )
    }

    private fun validateSmsCode() {
        val currentState = state()
        val validation = ValidationUtils.validateSmsCode(currentState.smsCode)

        dispatch(
            SignInReducer.Message.SetFieldError(
                emailOrPhoneError = currentState.emailOrPhoneError,
                passwordError = currentState.passwordError,
                smsCodeError = validation.errorMessage
            )
        )
    }

    private fun validateAllFields() {
        validateEmailOrPhone()

        val currentState = state()
        when (currentState.loginMethod) {
            LoginMethod.PASSWORD -> validatePassword()
            LoginMethod.SMS -> validateSmsCode()
        }

        updateButtonState()
    }

    private fun updateButtonState() {
        val currentState = state()
        // Enable button when fields are not empty (don't validate until login attempt)
        val emailOrPhoneNotEmpty = currentState.emailOrPhone.isNotBlank()
        val inputType = detectInputType(currentState.emailOrPhone)

        val secondFieldValid = when (currentState.loginMethod) {
            LoginMethod.PASSWORD -> currentState.password.isNotBlank()
            LoginMethod.SMS -> {
                // For SMS, button is enabled if phone is entered and SMS is sent, and code is entered
                if (inputType == InputType.PHONE && currentState.isSmsCodeSent) {
                    currentState.smsCode.isNotBlank()
                } else {
                    // If SMS not sent yet, enable button to allow SMS sending
                    inputType == InputType.PHONE
                }
            }
        }

        dispatch(SignInReducer.Message.UpdateButtonState(emailOrPhoneNotEmpty && secondFieldValid))
    }
}
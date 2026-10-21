package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
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
        if (state().isLoading && intent != Intent.MessageHasBeenShowed) return
        when (intent) {
            is Intent.Login -> login()
            is Intent.UpdateEmailOrPhone -> updateEmailOrPhone(intent.input)
            is Intent.UpdatePassword -> updatePassword(intent.password)
            is Intent.SetLoginMethod -> setLoginMethod(intent.method)
            is Intent.SetEmailOrPhoneFocus -> setEmailOrPhoneFocus(intent.focused)
            is Intent.SetPasswordFocus -> setPasswordFocus(intent.focused)
            is Intent.SignUp -> publish(Label.GoSignUp)
            is Intent.MessageHasBeenShowed -> dispatch(SignInReducer.Message.MessageConsumed)
        }
    }

    private fun login() {
        if (state().loginMethod == LoginMethod.PASSWORD) loginWithPassword()
    }

    private fun loginWithPassword() {
        // Validate all fields before login
        validateAllFields()

        val currentState = state()
        if (!currentState.enabled) return

        dispatch(SignInReducer.Message.Loading)
        scope.launch {

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
                    val message = it.mapToSauceError().mapToString()
                    exceptionTracker.reportException(it, key = "#SignInExecutor#loginWithPassword")
                    dispatch(SignInReducer.Message.Error(message))
                })
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
                    passwordError = state().passwordError
                )
            )
        } else {
            // Clear error when field is empty
            dispatch(
                SignInReducer.Message.SetFieldError(
                    emailOrPhoneError = null,
                    passwordError = state().passwordError
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

    private fun validateEmailOrPhone() {
        val currentState = state()
        val inputType = detectInputType(currentState.emailOrPhone)
        val validation = ValidationUtils.validateInput(currentState.emailOrPhone, inputType)

        dispatch(
            SignInReducer.Message.SetFieldError(
                emailOrPhoneError = validation.errorMessage,
                passwordError = currentState.passwordError
            )
        )
    }

    private fun validatePassword() {
        val currentState = state()
        val validation = ValidationUtils.validatePassword(currentState.password)

        dispatch(
            SignInReducer.Message.SetFieldError(
                emailOrPhoneError = currentState.emailOrPhoneError,
                passwordError = validation.errorMessage
            )
        )
    }

    private fun validateAllFields() {
        validateEmailOrPhone()
        validatePassword()
        updateButtonState()
    }

    private fun updateButtonState() {
        val currentState = state()
        dispatch(SignInReducer.Message.UpdateButtonState(
            currentState.loginMethod == LoginMethod.PASSWORD &&
                currentState.emailOrPhone.isNotBlank() && currentState.password.isNotBlank() &&
                currentState.emailOrPhoneError == null && currentState.passwordError == null,
        ))
    }
}

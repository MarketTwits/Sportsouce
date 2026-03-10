package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.use_case.SignUpUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStoreFactory.Msg.*
import kotlinx.coroutines.launch

internal class SignUpStoreFactory(
    private val storeFactory: StoreFactory,
    private val exceptionTracker: ExceptionTracker,
    private val useCase: SignUpUseCase,
) {
    fun create(): SignUpStore =
        object : SignUpStore,
            Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> by storeFactory.create(
                name = "SignUpStore",
                initialState = SignUpStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data class OnUpdateStage(val signUpStage: SignUpStage) : Msg
        data object OnConsumedEvent : Msg
        data class OnValueChanged(val statement: SignUpStatement) : Msg
        data object Loading : Msg
        data class LoadFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SignUpStore.Intent, Unit, SignUpStore.State, Msg, SignUpStore.Label>() {
        override fun executeIntent(intent: SignUpStore.Intent) {
            when (intent) {
                is SignUpStore.Intent.OnClickRegister -> {
                    register(state().statement)
                }

                is SignUpStore.Intent.ChangeValue -> dispatch(OnValueChanged(intent.statement))
                is SignUpStore.Intent.OnConsumedEvent -> dispatch(OnConsumedEvent)
                is SignUpStore.Intent.OnClickNext -> {
                    val currentStage = state().currentStage
                    val statement = state().statement

                    // Validate current stage before proceeding
                    if (validateStage(currentStage, statement)) {
                        val nextStage = when (currentStage) {
                            SignUpStage.FIRST -> SignUpStage.SECOND
                            SignUpStage.SECOND -> SignUpStage.THIRD
                            SignUpStage.THIRD -> return // Already at last stage
                        }
                        dispatch(OnUpdateStage(nextStage))
                    } else {
                        dispatch(LoadFailed(getValidationErrorMessage(currentStage, statement)))
                    }
                }

                is SignUpStore.Intent.OnClickBack -> {
                    val currentStage = state().currentStage
                    val previousStage = when (currentStage) {
                        SignUpStage.FIRST -> {
                            publish(SignUpStore.Label.OnClickBack)
                            return
                        }

                        SignUpStage.SECOND -> SignUpStage.FIRST
                        SignUpStage.THIRD -> SignUpStage.SECOND
                    }
                    dispatch(OnUpdateStage(previousStage))
                }

                is SignUpStore.Intent.OnClickSignIn -> publish(SignUpStore.Label.OpenSignIn)
            }
        }

        private fun validateStage(stage: SignUpStage, statement: SignUpStatement): Boolean {
            return when (stage) {
                SignUpStage.FIRST -> {
                    statement.phone.isNotBlank() && statement.email.isNotBlank() &&
                            isValidEmail(statement.email) && isValidPhone(statement.phone)
                }

                SignUpStage.SECOND -> {
                    statement.name.isNotBlank() && statement.surname.isNotBlank() &&
                            statement.sex.isNotBlank() && statement.birthday.isNotBlank()
                }

                SignUpStage.THIRD -> {
                    statement.password.isNotBlank() && statement.repeatPassword.isNotBlank() &&
                            statement.password == statement.repeatPassword && statement.password.length >= 6
                }
            }
        }

        private fun getValidationErrorMessage(stage: SignUpStage, statement: SignUpStatement): String {
            return when (stage) {
                SignUpStage.FIRST -> {
                    when {
                        statement.phone.isBlank() -> "Введите номер телефона"
                        statement.email.isBlank() -> "Введите email"
                        !isValidEmail(statement.email) -> "Введите корректный email"
                        !isValidPhone(statement.phone) -> "Введите корректный номер телефона"
                        else -> "Заполните все поля первого этапа"
                    }
                }

                SignUpStage.SECOND -> {
                    when {
                        statement.name.isBlank() -> "Введите имя"
                        statement.surname.isBlank() -> "Введите фамилию"
                        statement.sex.isBlank() -> "Выберите пол"
                        statement.birthday.isBlank() -> "Выберите дату рождения"
                        else -> "Заполните все поля второго этапа"
                    }
                }

                SignUpStage.THIRD -> {
                    when {
                        statement.password.isBlank() -> "Введите пароль"
                        statement.repeatPassword.isBlank() -> "Подтвердите пароль"
                        statement.password != statement.repeatPassword -> "Пароли не совпадают"
                        statement.password.length < 6 -> "Пароль должен содержать минимум 6 символов"
                        else -> "Заполните все поля третьего этапа"
                    }
                }
            }
        }

        private fun isValidEmail(email: String): Boolean {
            return email.contains("@") && email.contains(".") && email.length > 5
        }

        private fun isValidPhone(phone: String): Boolean {
            val cleanPhone = phone.replace(Regex("[^0-9]"), "")
            return cleanPhone.length >= 10
        }

        private fun register(statement: SignUpStatement) {
            scope.launch {
                dispatch(Loading)
                useCase.registry(statement).fold(
                    onSuccess = {
                        publish(
                            SignUpStore.Label.OpenProfile(
                                phone = statement.phone,
                                password = statement.password
                            )
                        )
                    }, onFailure = {
                        exceptionTracker.reportException(it, key = "sign_up")
                        dispatch(LoadFailed(it.mapToSauceError().mapToString()))
                    })

            }
        }
    }

    private object ReducerImpl : Reducer<SignUpStore.State, Msg> {
        override fun SignUpStore.State.reduce(msg: Msg): SignUpStore.State =
            when (msg) {
                is LoadFailed -> copy(
                    isLoading = false,
                    event = triggered(EventContent(false, msg.message))
                )

                is Loading -> copy(isLoading = true)
                is OnValueChanged -> copy(statement = msg.statement)
                is OnConsumedEvent -> copy(event = consumed())
                is OnUpdateStage -> copy(currentStage = msg.signUpStage)
            }
    }
}

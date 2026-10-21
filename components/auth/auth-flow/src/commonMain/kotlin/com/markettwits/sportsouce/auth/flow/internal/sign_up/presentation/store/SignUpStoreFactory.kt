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
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpFieldErrors
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation.SignUpValidation
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.use_case.SignUpUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStoreFactory.Msg.*
import kotlinx.coroutines.launch

internal class SignUpStoreFactory(
    private val storeFactory: StoreFactory,
    private val exceptionTracker: ExceptionTracker,
    private val useCase: SignUpUseCase,
    private val validation: SignUpValidation,
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
        data class OnValueChanged(val statement: SignUpStatement, val errors: SignUpFieldErrors) : Msg
        data class Validated(
            val errors: SignUpFieldErrors,
            val stages: Set<SignUpStage>,
            val stage: SignUpStage,
            val failed: Boolean,
        ) : Msg
        data object Loading : Msg
        data class LoadFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SignUpStore.Intent, Unit, SignUpStore.State, Msg, SignUpStore.Label>() {
        override fun executeIntent(intent: SignUpStore.Intent) {
            if (state().isLoading && intent != SignUpStore.Intent.OnConsumedEvent) return
            when (intent) {
                is SignUpStore.Intent.OnClickRegister -> {
                    register(state().statement)
                }

                is SignUpStore.Intent.ChangeValue -> dispatch(OnValueChanged(
                    intent.statement,
                    validation.errors(intent.statement, state().validatedStages),
                ))
                is SignUpStore.Intent.OnConsumedEvent -> dispatch(OnConsumedEvent)
                is SignUpStore.Intent.OnClickNext -> {
                    val stage = state().currentStage
                    if (stage == SignUpStage.THIRD) return
                    val stages = state().validatedStages + stage
                    val errors = validation.errors(state().statement, stages)
                    val failed = validation.errors(state().statement, setOf(stage)).firstInvalidStage() != null
                    dispatch(Validated(
                        errors = errors,
                        stages = stages,
                        stage = if (failed) stage else SignUpStage.fromIndex(stage.index + 1),
                        failed = failed,
                    ))
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

        private fun register(statement: SignUpStatement) {
            val stages = SignUpStage.entries.toSet()
            val errors = validation.errors(statement, stages)
            val invalidStage = errors.firstInvalidStage()
            dispatch(Validated(errors, stages, invalidStage ?: state().currentStage, invalidStage != null))
            if (invalidStage != null) return
            dispatch(Loading)
            scope.launch {
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
                is OnValueChanged -> copy(statement = msg.statement, errors = msg.errors)
                is Validated -> copy(
                    errors = msg.errors,
                    validatedStages = msg.stages,
                    currentStage = msg.stage,
                    validationAttempt = validationAttempt + if (msg.failed) 1 else 0,
                    event = consumed(),
                )
                is OnConsumedEvent -> copy(event = consumed())
                is OnUpdateStage -> copy(currentStage = msg.signUpStage)
            }
    }
}

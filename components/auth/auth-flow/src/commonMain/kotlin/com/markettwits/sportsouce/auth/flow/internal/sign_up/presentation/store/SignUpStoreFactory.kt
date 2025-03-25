package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.use_case.SignUpUseCase
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
        data object OnConsumedEvent : Msg
        data class OnValueChanged(val statement: SignUpStatement) : Msg
        data object Loading : Msg
        data class LoadFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SignUpStore.Intent, Unit, SignUpStore.State, Msg, SignUpStore.Label>() {
        override fun executeIntent(intent: SignUpStore.Intent) {
            when (intent) {
                is SignUpStore.Intent.OnClickBack -> publish(SignUpStore.Label.OnClickBack)
                is SignUpStore.Intent.OnClickRegister -> register(state().statement)
                is SignUpStore.Intent.ChangeValue -> dispatch(Msg.OnValueChanged(intent.statement))
                is SignUpStore.Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
            }
        }

        private fun register(statement: SignUpStatement) {
            scope.launch {
                dispatch(Msg.Loading)
                useCase.registry(statement).fold(
                    onSuccess = {
                        publish(SignUpStore.Label.OpenProfile)
                    }, onFailure = {
                        exceptionTracker.reportException(it, key = "sign_up")
                        dispatch(Msg.LoadFailed(it.networkExceptionHandler().message.toString()))
                    })

            }
        }
    }

    private object ReducerImpl : Reducer<SignUpStore.State, Msg> {
        override fun SignUpStore.State.reduce(msg: Msg): SignUpStore.State =
            when (msg) {
                is Msg.LoadFailed -> copy(
                    isLoading = false,
                    event = triggered(EventContent(false, msg.message))
                )
                is Msg.Loading -> copy(isLoading = true)
                is Msg.OnValueChanged -> copy(statement = msg.statement)
                is Msg.OnConsumedEvent -> copy(event = consumed())
            }
    }
}

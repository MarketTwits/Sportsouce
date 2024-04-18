package com.markettwits.profile.internal.sign_up.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.crashlitics.api.tracker.AnalyticsTracker
import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.profile.internal.sign_up.domain.use_case.SignUpUseCase
import kotlinx.coroutines.launch

internal class SignUpStoreFactory(
    private val storeFactory: StoreFactory,
    private val analyticsTracker: AnalyticsTracker,
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
        override fun executeIntent(intent: SignUpStore.Intent, getState: () -> SignUpStore.State) {
            when (intent) {
                is SignUpStore.Intent.OnClickBack -> publish(SignUpStore.Label.OnClickBack)
                is SignUpStore.Intent.OnClickRegister -> register(getState().statement)
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
                        analyticsTracker.reportException(it, key = "sign_up")
                        dispatch(Msg.LoadFailed(networkExceptionHandler(it).message.toString()))
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

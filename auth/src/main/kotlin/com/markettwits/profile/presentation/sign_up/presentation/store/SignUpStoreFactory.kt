package com.markettwits.profile.presentation.sign_up.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement
import com.markettwits.profile.presentation.sign_up.domain.SignUpValidation
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch

class SignUpStoreFactory(
    private val storeFactory: StoreFactory,
    private val registerRepository: AuthDataSource,
    private val signUpValidation: SignUpValidation
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
                is SignUpStore.Intent.OnClickRegister -> launch(getState().statement)
                is SignUpStore.Intent.ChangeValue -> dispatch(Msg.OnValueChanged(intent.statement))
                is SignUpStore.Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
            }
        }

        override fun executeAction(action: Unit, getState: () -> SignUpStore.State) = Unit
        private fun launch(statement: SignUpStatement) {
            scope.launch {
                dispatch(Msg.Loading)
                signUpValidation.validate(statement).fold(onSuccess = {
                    launchInner(it)
                }, onFailure = {
                    dispatch(Msg.LoadFailed(it.message.toString()))
                })
            }
        }

        private fun launchInner(statement: SignUpStatement) {
            scope.launch {
                dispatch(Msg.Loading)
                registerRepository.register(statement).fold(
                    onSuccess = {
                        publish(SignUpStore.Label.OpenProfile)
                    },
                    onFailure = {
                        val message = when (it) {
                            is ClientRequestException -> it.response.body<AuthErrorResponse>().message
                            else -> it.message.toString()
                        }
                        dispatch(Msg.LoadFailed(message))
                    }
                )
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

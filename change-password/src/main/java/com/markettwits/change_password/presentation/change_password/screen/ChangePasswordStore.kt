package com.markettwits.change_password.presentation.change_password.screen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.change_password.data.ChangePasswordDataSource
import com.markettwits.change_password.domain.ChangePasswordValidation
import com.markettwits.change_password.presentation.change_password.screen.ChangePasswordStore.Intent
import com.markettwits.change_password.presentation.change_password.screen.ChangePasswordStore.Label
import com.markettwits.change_password.presentation.change_password.screen.ChangePasswordStore.State
import com.markettwits.core_ui.event.StateEvent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.launch
import okhttp3.internal.platform.android.AndroidLogHandler.publish
import ru.alexpanov.core_network.api.SportsouceApi

interface ChangePasswordStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class ChangeCurrentPassword(val value: String) : Intent
        data class ChangeNewPassword(val value: String) : Intent
        data class ChangeNewRepeatPassword(val value: String) : Intent
        data object ChangePassword : Intent
        data object OnConsumedSucceededEvent : Intent
        data object OnConsumedFailedEvent : Intent
        data object Pop : Intent
    }

    data class State(
        val currentPassword: String,
        val newPassword: String,
        val newRepeatPassword: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        //TODO
        val downloadSucceededEvent: StateEventWithContent<String> = consumed(),
        val downloadFailedEvent: StateEventWithContent<String> = consumed(),
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class ShowSuccess(val message: String) : Label
        data object GoBack : Label
        data object Launch : Label
    }
}

class ChangePasswordStoreFactory(
    private val storeFactory: StoreFactory,
    private val changePasswordDataSource: ChangePasswordDataSource,
    private val validation: ChangePasswordValidation
) {

    fun create(): ChangePasswordStore =
        object : ChangePasswordStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChangePasswordStore",
            initialState = State("", "", "", false, false, ""),
            //  bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    sealed interface Action

    private sealed interface Msg {
        data object Loading : Msg
        data object OnConsumedSucceededEvent : Msg
        data object OnConsumedFailedEvent : Msg
        data class UpdateFailed(val message: String) : Msg
        data class UpdateSuccess(val message: String) : Msg
        data class ChangePassword(val password: String) : Msg
        data class ChangeNewPassword(val password: String) : Msg
        data class ChangeNewRepeatPassword(val password: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeCurrentPassword -> {
                    dispatch(Msg.ChangePassword(intent.value))
                }

                is Intent.ChangeNewPassword -> {
                    dispatch(Msg.ChangeNewPassword(intent.value))
                }

                is Intent.ChangeNewRepeatPassword -> {
                    dispatch(Msg.ChangeNewRepeatPassword(intent.value))
                }
                is Intent.OnConsumedFailedEvent ->{
                    dispatch(Msg.OnConsumedFailedEvent)
                }
                is Intent.OnConsumedSucceededEvent-> {
                    dispatch(Msg.OnConsumedSucceededEvent)
                }

                is Intent.ChangePassword -> {
                    val state = getState()
                    scope.launch {
                        val validResult = validation.changePassword(
                            oldPassword = state.currentPassword,
                            newPassword1 = state.newPassword,
                            newPassword2 = state.newRepeatPassword
                        )
                        validResult.onFailure {
                            publish(Label.ShowError(it.message.toString()))

                            dispatch(Msg.UpdateFailed(it.message.toString()))
                        }
                            .onSuccess {
                                dispatch(Msg.Loading)
                                changePasswordDataSource.changePasswordNew(
                                    password = state.currentPassword,
                                    newPassword = state.newPassword
                                ).onSuccess {
                                    dispatch(Msg.UpdateSuccess("Данные успешно обновлены"))
                                    publish(Label.ShowSuccess("Данные успешно"))
                                }
                                    .onFailure {
                                        publish(Label.ShowError(it.message.toString()))
                                        dispatch(Msg.UpdateFailed(it.message.toString()))
                                    }
                            }
                    }
                }

                is Intent.Pop -> publish(Label.GoBack)
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.ChangePassword -> copy(currentPassword = message.password)
                is Msg.ChangeNewPassword -> copy(newPassword = message.password)
                is Msg.ChangeNewRepeatPassword -> copy(newRepeatPassword = message.password)
                is Msg.Loading -> copy(isLoading = true)
                is Msg.UpdateFailed -> copy(
                    isError = true,
                    message = message.message,
                    isLoading = false,
                    downloadFailedEvent = triggered(message.message)
                )
                is Msg.UpdateSuccess -> copy(isLoading = false, isError = false, downloadSucceededEvent = triggered("Данные успешно обновлены"))
                is Msg.OnConsumedFailedEvent -> copy(downloadFailedEvent = consumed())
                is Msg.OnConsumedSucceededEvent -> copy(downloadSucceededEvent = consumed())
            }
    }
}

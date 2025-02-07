package com.markettwits.edit_profile.edit_profile_change_password.presentation.screen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.edit_profile.edit_profile_change_password.data.ChangePasswordDataSource
import com.markettwits.edit_profile.edit_profile_change_password.domain.ChangePasswordValidation
import com.markettwits.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordStore.Intent
import com.markettwits.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordStore.Label
import com.markettwits.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordStore.State
import kotlinx.coroutines.launch

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
        val message: String,
        val downloadSucceededEvent: StateEventWithContent<String> = consumed(),
        val downloadFailedEvent: StateEventWithContent<String> = consumed(),
    )

    sealed interface Label {
        data object GoBack : Label
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
            initialState = State("", "", "", false, ""),
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
        override fun executeIntent(intent: Intent) {
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

                is Intent.OnConsumedFailedEvent -> {
                    dispatch(Msg.OnConsumedFailedEvent)
                }

                is Intent.OnConsumedSucceededEvent -> {
                    dispatch(Msg.OnConsumedSucceededEvent)
                }

                is Intent.ChangePassword -> {
                    val state = state()
                    scope.launch {
                        val validResult = validation.changePassword(
                            oldPassword = state.currentPassword,
                            newPassword1 = state.newPassword,
                            newPassword2 = state.newRepeatPassword
                        )
                        validResult.onFailure {
                            dispatch(Msg.UpdateFailed(it.message.toString()))
                        }
                            .onSuccess {
                                dispatch(Msg.Loading)
                                changePasswordDataSource.changePassword(
                                    password = state.currentPassword,
                                    newPassword = state.newPassword
                                ).onSuccess {
                                    dispatch(Msg.UpdateSuccess("Данные успешно обновлены"))
                                }
                                    .onFailure {
                                        dispatch(Msg.UpdateFailed(networkExceptionHandler(it).message.toString()))
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
                    message = message.message,
                    isLoading = false,
                    downloadFailedEvent = triggered(message.message)
                )

                is Msg.UpdateSuccess -> copy(
                    isLoading = false,
                    downloadSucceededEvent = triggered(message.message)
                )

                is Msg.OnConsumedFailedEvent -> copy(downloadFailedEvent = consumed())
                is Msg.OnConsumedSucceededEvent -> copy(downloadSucceededEvent = consumed())
            }
    }
}

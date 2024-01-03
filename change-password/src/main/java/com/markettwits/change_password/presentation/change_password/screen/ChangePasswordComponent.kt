package com.markettwits.change_password.presentation.change_password.screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class ChangePasswordComponent(
    context: ComponentContext,
    private val pop: () -> Unit,
    private val storeFactory: ChangePasswordStoreFactory
) : ChangePassword,
    ComponentContext by context {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val newEvents: MutableStateFlow<ChangePasswordEvent> = MutableStateFlow(ChangePasswordEvent.ShowSuccess(""))

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ChangePasswordStore.Label.GoBack -> pop()
                    is ChangePasswordStore.Label.ShowError -> {
                        newEvents.emit(ChangePasswordEvent.ShowError(it.message))
                       // sendEventSync(ChangePasswordEvent.ShowError(it.message))
                    }
                    is ChangePasswordStore.Label.ShowSuccess -> {
                        newEvents.emit(ChangePasswordEvent.ShowSuccess(it.message))
                     //   sendEventSync(ChangePasswordEvent.ShowSuccess(it.message))
                    }
                    is ChangePasswordStore.Label.Launch -> {

                    }
                }
            }
        }
    }


    override val state: StateFlow<ChangePasswordStore.State> = store.stateFlow
    private val _event = Channel<ChangePasswordEvent>()
    override val events = _event.receiveAsFlow().shareIn(scope, SharingStarted.Lazily)
    protected fun sendEventSync(event: ChangePasswordEvent) = scope.launch { _event.send(event) }

    override fun onCurrentPasswordChanged(value: String) {
        store.accept(ChangePasswordStore.Intent.ChangeCurrentPassword(value))
    }

    override fun onNewPasswordChanged(value: String) {
        store.accept(ChangePasswordStore.Intent.ChangeNewPassword(value))
    }

    override fun onNewPasswordRepeatChanged(value: String) {
        store.accept(ChangePasswordStore.Intent.ChangeNewRepeatPassword(value))
    }

    override fun onSaveChanged() {
        store.accept(ChangePasswordStore.Intent.ChangePassword)
    }

    override fun onConsumedDownloadSucceededEvent() {
        store.accept(ChangePasswordStore.Intent.OnConsumedSucceededEvent)
    }

    override fun onConsumedDownloadFailedEvent() {
        store.accept(ChangePasswordStore.Intent.OnConsumedFailedEvent)
    }

    override fun back() {
        store.accept(ChangePasswordStore.Intent.Pop)
    }
}
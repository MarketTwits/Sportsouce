package com.markettwits.edit_profile.edit_profile_change_password.presentation.screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
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

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ChangePasswordStore.Label.GoBack -> pop()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ChangePasswordStore.State> = store.stateFlow

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
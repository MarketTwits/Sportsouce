package com.markettwits.change_password.presentation.screen

import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.core_ui.event.consumed
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ChangePassword {
    val state : StateFlow<ChangePasswordStore.State>
    fun onCurrentPasswordChanged(value : String)
    fun onNewPasswordChanged(value: String)
    fun onNewPasswordRepeatChanged(value: String)
    fun onSaveChanged()
    fun onConsumedDownloadSucceededEvent()
    fun onConsumedDownloadFailedEvent()
    fun back()
}